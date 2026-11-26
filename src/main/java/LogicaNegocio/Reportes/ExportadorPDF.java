package LogicaNegocio.Reportes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ExportadorPDF implements ExportadorReporte {

    private static final int LINEAS_POR_PAGINA = 45;
    private static final Charset CP1252 = Charset.forName("windows-1252");
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public String extension() { return "pdf"; }

    @Override
    public void exportar(ReporteDiario reporte, Path destino) throws IOException {
        Files.write(destino, construirPdf(construirLineas(reporte)));
    }

    private List<String> construirLineas(ReporteDiario r) {
        List<String> l = new ArrayList<>();
        l.add("REPORTE DE VENTAS DEL DÍA");
        if (r.getVendedor() != null) {
            l.add("Vendedor: " + r.getVendedor());
        }
        l.add("Fecha: " + r.getFecha());
        l.add("");
        l.add("Ingresos totales: " + ReporteDiario.dinero(r.getIngresosTotales()));
        l.add("Número de ventas: " + r.getNumeroVentas());
        if (r.getVendedor() != null) {
            l.add("(Valores antes de los descuentos aplicados al pedido.)");
        }
        l.add("");
        l.add("Productos vendidos (de mayor a menor)");
        if (r.getProductosVendidos().isEmpty()) {
            l.add("Sin ventas registradas en esta fecha.");
        } else {
            int i = 1;
            for (ProductoVendido p : r.getProductosVendidos()) {
                l.add(cortar(i + ". " + p.getNombre(), 50) + "   x" + p.getUnidades()
                        + "   " + ReporteDiario.dinero(p.getIngresos()));
                i++;
            }
        }

        // detalle por pedido (solo si el reporte trae las líneas de venta)
        if (!r.getLineas().isEmpty()) {
            l.add("");
            l.add("Detalle por pedido");
            int pedidoActual = -1;
            for (LineaVenta v : r.getLineas()) {
                if (v.getIdPedido() != pedidoActual) {
                    pedidoActual = v.getIdPedido();
                    l.add("Pedido #" + v.getIdPedido() + "  " + v.getFecha().format(HORA)
                            + "  " + cortar(v.getCliente(), 30));
                    l.add("   " + v.getMedioPago().getEtiqueta() + " - " + v.getEstado().getEtiqueta());
                }
                l.add("   " + v.getCantidad() + " x " + cortar(v.getProducto(), 36)
                        + "   " + ReporteDiario.dinero(v.getSubtotal()));
            }
        }
        return l;
    }

    private byte[] construirPdf(List<String> lineas) throws IOException {
        // Dividir en páginas
        List<List<String>> paginas = new ArrayList<>();
        for (int i = 0; i < lineas.size(); i += LINEAS_POR_PAGINA) {
            paginas.add(lineas.subList(i, Math.min(i + LINEAS_POR_PAGINA, lineas.size())));
        }
        int n = paginas.size();

        // Objetos: 1 = catálogo, 2 = páginas, 3 = fuente,
        // luego por cada página k: objeto 4+2k (página) y 5+2k (contenido)
        List<byte[]> objetos = new ArrayList<>();
        objetos.add(ascii("<< /Type /Catalog /Pages 2 0 R >>"));

        StringBuilder kids = new StringBuilder();
        for (int k = 0; k < n; k++) {
            kids.append(4 + 2 * k).append(" 0 R ");
        }
        objetos.add(ascii("<< /Type /Pages /Kids [" + kids + "] /Count " + n + " >>"));
        objetos.add(ascii("<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica /Encoding /WinAnsiEncoding >>"));

        for (int k = 0; k < n; k++) {
            objetos.add(ascii("<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] "
                    + "/Resources << /Font << /F1 3 0 R >> >> /Contents " + (5 + 2 * k) + " 0 R >>"));
            byte[] contenido = contenidoPagina(paginas.get(k));
            ByteArrayOutputStream s = new ByteArrayOutputStream();
            s.write(ascii("<< /Length " + contenido.length + " >>\nstream\n"));
            s.write(contenido);
            s.write(ascii("\nendstream"));
            objetos.add(s.toByteArray());
        }

        // Ensamblar archivo y tabla xref
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(ascii("%PDF-1.4\n"));
        int[] offsets = new int[objetos.size()];
        for (int i = 0; i < objetos.size(); i++) {
            offsets[i] = out.size();
            out.write(ascii((i + 1) + " 0 obj\n"));
            out.write(objetos.get(i));
            out.write(ascii("\nendobj\n"));
        }
        int xref = out.size();
        out.write(ascii("xref\n0 " + (objetos.size() + 1) + "\n"));
        out.write(ascii("0000000000 65535 f \n"));
        for (int off : offsets) {
            out.write(ascii(String.format(Locale.ROOT, "%010d 00000 n \n", off)));
        }
        out.write(ascii("trailer\n<< /Size " + (objetos.size() + 1) + " /Root 1 0 R >>\n"
                + "startxref\n" + xref + "\n%%EOF\n"));
        return out.toByteArray();
    }

    private byte[] contenidoPagina(List<String> lineas) {
        StringBuilder sb = new StringBuilder();
        sb.append("BT\n/F1 12 Tf\n14 TL\n50 790 Td\n");
        for (String linea : lineas) {
            sb.append('(').append(escapar(linea)).append(") Tj T*\n");
        }
        sb.append("ET");
        return sb.toString().getBytes(CP1252);
    }

    private static String escapar(String s) {
        return s.replaceAll("[\\r\\n\\t]", " ")
                .replace("\\", "\\\\")
                .replace("(", "\\(")
                .replace(")", "\\)");
    }

    private static String cortar(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max - 3) + "...";
    }

    private static byte[] ascii(String s) {
        return s.getBytes(CP1252);
    }
}