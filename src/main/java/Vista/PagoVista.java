package Vista;

import LogicaNegocio.Carrito.CarritoException;
import LogicaNegocio.Descuentos.ResultadoDescuento;
import LogicaNegocio.Moneda;
import LogicaNegocio.PagosStrategy.DatosPago;
import LogicaNegocio.PagosStrategy.MedioPago;
import LogicaNegocio.PagosStrategy.PagoTransferencia;
import LogicaNegocio.Pedido.ConfirmacionCompra;
import LogicaNegocio.Pedido.PedidoException;
import LogicaNegocio.Pedido.PedidoService;
import LogicaNegocio.Usuario;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;


public class PagoVista extends JDialog {

    private final Contenido contenido;
    private final boolean listo; // false si no hay nada que pagar (carrito vacío)

    public PagoVista(Window propietario, Usuario cliente, PedidoService pedidos) {
        super(propietario, "Pago", ModalityType.APPLICATION_MODAL);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        this.contenido = new Contenido(cliente, pedidos, this::dispose);
        setContentPane(contenido);
        getRootPane().setDefaultButton(contenido.getBotonPagar()); // Enter = PAGAR
        pack();
        setLocationRelativeTo(propietario);
        this.listo = contenido.actualizarResumen();
    }


    public boolean isCompraRealizada() {
        return contenido.isCompraRealizada();
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible && listo);
    }


    public static class Contenido extends JPanel {

        private static final Color FONDO = new Color(204, 255, 204); // el mismo verde de la vista del cliente

        private final Usuario cliente;
        private final PedidoService pedidos;
        private final Runnable cerrar;

        private final JTextField campoCupon = new JTextField(10);
        private final JLabel etiquetaSubtotal = new JLabel("-");
        private final JLabel etiquetaDescuento = new JLabel("-");
        private final JLabel etiquetaTotal = new JLabel("-");
        private final JLabel etiquetaNota = new JLabel(" ");

        private final JComboBox<MedioPago> comboMedio = new JComboBox<>();
        private final CardLayout cartas = new CardLayout();
        private final JPanel panelMedio = new JPanel(cartas);

        private final JTextField campoNumero = new JTextField(16);
        private final JTextField campoVencimiento = new JTextField(5);
        private final JPasswordField campoCvv = new JPasswordField(4);
        private final JComboBox<String> comboBanco =
                new JComboBox<>(PagoTransferencia.BANCOS_DISPONIBLES.toArray(new String[0]));

        private final JTextField campoDireccion = new JTextField(22);
        private final JButton botonPagar = new JButton("PAGAR");
        private final JButton botonCancelar = new JButton("CANCELAR");

        private String cuponAplicado = null; // el cupón con el que se calculó el total que se está mostrando
        private boolean compraRealizada = false;

        public Contenido(Usuario cliente, PedidoService pedidos, Runnable cerrar) {
            super(new GridBagLayout());
            this.cliente = cliente;
            this.pedidos = pedidos;
            this.cerrar = cerrar;
            construir();
            configurar();
        }

        public boolean isCompraRealizada() {
            return compraRealizada;
        }

        public JButton getBotonPagar() {
            return botonPagar;
        }

        // todos los avisos pasan por aquí
        protected void mensaje(String titulo, String texto, int tipo) {
            JOptionPane.showMessageDialog(this, texto, titulo, tipo);
        }


        private void construir() {
            setBackground(FONDO);
            setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

            JLabel titulo = new JLabel("PAGO", JLabel.CENTER);
            titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 16f));
            etiquetaTotal.setFont(etiquetaTotal.getFont().deriveFont(Font.BOLD, 14f));

            JPanel filaCupon = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
            filaCupon.setOpaque(false);
            JButton botonCupon = new JButton("Aplicar");
            botonCupon.addActionListener(e -> actualizarResumen());
            campoCupon.addActionListener(e -> actualizarResumen()); // Enter también aplica
            filaCupon.add(campoCupon);
            filaCupon.add(botonCupon);

            panelMedio.setOpaque(false);
            panelMedio.add(panelTarjeta(), MedioPago.TARJETA.name());
            panelMedio.add(panelTransferencia(), MedioPago.TRANSFERENCIA.name());
            panelMedio.add(panelContraEntrega(), MedioPago.CONTRA_ENTREGA.name());

            JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
            botones.setOpaque(false);
            botones.add(botonPagar);
            botones.add(botonCancelar);

            int y = 0;
            agregar(this, titulo, 0, y++, 2);
            agregar(this, new JLabel("Cupón"), 0, y, 1);
            agregar(this, filaCupon, 1, y++, 1);
            agregar(this, etiquetaNota, 0, y++, 2);
            agregar(this, new JLabel("Subtotal"), 0, y, 1);
            agregar(this, etiquetaSubtotal, 1, y++, 1);
            agregar(this, new JLabel("Descuento"), 0, y, 1);
            agregar(this, etiquetaDescuento, 1, y++, 1);
            agregar(this, new JLabel("TOTAL A PAGAR"), 0, y, 1);
            agregar(this, etiquetaTotal, 1, y++, 1);
            agregar(this, new JSeparator(), 0, y++, 2);
            agregar(this, new JLabel("Método de pago"), 0, y, 1);
            agregar(this, comboMedio, 1, y++, 1);
            agregar(this, panelMedio, 0, y++, 2);
            agregar(this, new JLabel("Dirección de envío"), 0, y, 1);
            agregar(this, campoDireccion, 1, y++, 1);
            agregar(this, botones, 0, y, 2);
        }

        private void agregar(JPanel panel, Component componente, int x, int y, int ancho) {
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = x;
            c.gridy = y;
            c.gridwidth = ancho;
            c.insets = new Insets(4, 4, 4, 4);
            c.anchor = GridBagConstraints.WEST;
            // la columna de valores ocupa todo el ancho, así los montos nunca quedan recortados
            c.fill = (ancho == 2 || x == 1) ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE;
            c.weightx = x == 1 || ancho == 2 ? 1 : 0;
            panel.add(componente, c);
        }

        private JPanel panelTarjeta() {
            JPanel p = new JPanel(new GridBagLayout());
            p.setOpaque(false);
            agregar(p, new JLabel("Número de tarjeta"), 0, 0, 1);
            agregar(p, campoNumero, 1, 0, 1);
            agregar(p, new JLabel("Vencimiento (MM/AA)"), 0, 1, 1);
            agregar(p, campoVencimiento, 1, 1, 1);
            agregar(p, new JLabel("CVV"), 0, 2, 1);
            agregar(p, campoCvv, 1, 2, 1);
            return p;
        }

        private JPanel panelTransferencia() {
            JPanel p = new JPanel(new GridBagLayout());
            p.setOpaque(false);
            agregar(p, new JLabel("Banco"), 0, 0, 1);
            agregar(p, comboBanco, 1, 0, 1);
            return p;
        }

        private JPanel panelContraEntrega() {
            JPanel p = new JPanel(new BorderLayout());
            p.setOpaque(false);
            p.add(new JLabel("Pagarás al recibir tu pedido. No necesitas datos de pago."), BorderLayout.CENTER);
            return p;
        }

        private void configurar() {
            for (MedioPago medio : pedidos.mediosDePago()) {
                comboMedio.addItem(medio);
            }
            // el combo muestra "Contra entrega" y no el nombre interno CONTRA_ENTREGA
            comboMedio.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> lista, Object valor, int indice,
                                                              boolean seleccionado, boolean foco) {
                    String texto = valor instanceof MedioPago m ? m.getEtiqueta() : String.valueOf(valor);
                    return super.getListCellRendererComponent(lista, texto, indice, seleccionado, foco);
                }
            });
            comboMedio.addActionListener(e -> mostrarCamposDelMedio());
            mostrarCamposDelMedio();

            botonPagar.addActionListener(e -> pagar());
            botonCancelar.addActionListener(e -> cerrar.run());
        }

        // aparecen solo los campos que necesita el método elegido
        private void mostrarCamposDelMedio() {
            MedioPago medio = (MedioPago) comboMedio.getSelectedItem();
            if (medio != null) {
                cartas.show(panelMedio, medio.name());
            }
        }


        private String cuponEscrito() {
            String texto = campoCupon.getText().trim();
            return texto.isEmpty() ? null : texto;
        }

        public boolean actualizarResumen() {
            String cupon = cuponEscrito();
            try {
                ResultadoDescuento r = pedidos.resumenCompra(cliente, cupon);
                etiquetaSubtotal.setText(Moneda.pesos(r.getSubtotal()));
                etiquetaDescuento.setText(r.getMontoDescuento().signum() == 0
                        ? "$0"
                        : "-" + Moneda.pesos(r.getMontoDescuento()) + " ("
                          + r.getPorcentajeAplicado().stripTrailingZeros().toPlainString() + "%)");
                etiquetaTotal.setText(Moneda.pesos(r.getTotal()));
                etiquetaNota.setText(notaDescuentos(r, cupon));
                cuponAplicado = cupon;
                return true;
            } catch (CarritoException | PedidoException ex) {
                mensaje("Pago", ex.getMessage(), JOptionPane.WARNING_MESSAGE);
                cerrar.run();
                return false;
            }
        }

        private String notaDescuentos(ResultadoDescuento r, String cupon) {
            boolean cuponValido = r.getDetalle().stream().anyMatch(d -> d.startsWith("Cupón"));
            if (cupon != null && !cuponValido) {
                return "Ese cupón no es válido.";
            }
            if (r.getDetalle().isEmpty()) {
                return " ";
            }
            return "Descuentos: " + String.join(" · ", r.getDetalle());
        }

        private DatosPago datosDelMedio(MedioPago medio) {
            return switch (medio) {
                case TARJETA -> DatosPago.tarjeta(campoNumero.getText(), campoVencimiento.getText(),
                        new String(campoCvv.getPassword()));
                case TRANSFERENCIA -> DatosPago.transferencia((String) comboBanco.getSelectedItem());
                case CONTRA_ENTREGA -> DatosPago.ninguno();
            };
        }

        private void pagar() {
            MedioPago medio = (MedioPago) comboMedio.getSelectedItem();
            if (medio == null) {
                mensaje("Pago", "Selecciona un método de pago.", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // si escribió un cupón distinto y no pulsó Aplicar, primero se muestra el total real
            String escrito = cuponEscrito();
            if (!java.util.Objects.equals(escrito, cuponAplicado)) {
                actualizarResumen();
                mensaje("Pago", "Actualizamos el total con tu cupón. Revísalo y vuelve a pulsar PAGAR.",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            botonPagar.setEnabled(false); // evita dos clics seguidos = dos pedidos
            try {
                ConfirmacionCompra c = pedidos.confirmarCompra(
                        cliente, medio, datosDelMedio(medio), campoDireccion.getText(), cuponAplicado);
                compraRealizada = true;
                campoCvv.setText(""); // el CVV no se queda en memoria de la pantalla
                mensaje("¡Compra realizada!", mensajeConfirmacion(c), JOptionPane.INFORMATION_MESSAGE);
                cerrar.run();
            } catch (CarritoException | PedidoException ex) {
                mensaje("No se pudo pagar", ex.getMessage(), JOptionPane.WARNING_MESSAGE);
            } catch (RuntimeException ex) {
                mensaje("Error", "No se pudo completar la compra. Revisa la conexión con la base de datos.\n"
                        + (ex.getMessage() == null ? "" : ex.getMessage()), JOptionPane.ERROR_MESSAGE);
            } finally {
                botonPagar.setEnabled(true);
            }
        }

        private String mensajeConfirmacion(ConfirmacionCompra c) {
            StringBuilder sb = new StringBuilder();
            sb.append("Pedido #").append(c.getNumeroPedido()).append('\n');
            sb.append("Estado: ").append(c.getPedido().getEstado().getEtiqueta()).append('\n');
            sb.append("Subtotal: ").append(Moneda.pesos(c.getPedido().getSubtotal())).append('\n');
            if (c.getPedido().getDescuento().signum() > 0) {
                sb.append("Descuento: -").append(Moneda.pesos(c.getPedido().getDescuento())).append('\n');
            }
            sb.append("Total: ").append(Moneda.pesos(c.getPedido().getTotal())).append("\n\n");
            sb.append(c.getPago().getMensaje()).append('\n');
            sb.append("Referencia: ").append(c.getPago().getReferencia()).append('\n');
            sb.append("Envío a: ").append(c.getPedido().getDireccionEnvio());
            return sb.toString();
        }
    }
}
