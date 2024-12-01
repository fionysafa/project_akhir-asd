import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class VisualisasiPengurutan extends JFrame {
    private JTextField masukanField;
    private JComboBox<String> metodeComboBox;
    private JButton tombolUrutkan;
    private PanelPengurutan panelPengurutan;
    private JLabel labelStatus;
    private int[] data;

    public VisualisasiPengurutan() {
        setTitle("Visualisasi Pengurutan");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panelAtas = new JPanel();
        panelAtas.setLayout(new BoxLayout(panelAtas, BoxLayout.Y_AXIS));
        panelAtas.setBackground(new Color(173, 216, 230));

        JPanel panelMasukan = new JPanel(new FlowLayout());
        JLabel labelMasukan = new JLabel("Masukkan angka, pisahkan dengan koma:");
        masukanField = new JTextField(25);
        panelMasukan.setBackground(new Color(173, 216, 230));
        panelMasukan.add(labelMasukan);
        panelMasukan.add(masukanField);

        JPanel panelMetode = new JPanel(new FlowLayout());
        JLabel labelMetode = new JLabel("Metode Pengurutan:");
        metodeComboBox = new JComboBox<>(new String[]{"Bubble Sort", "Insertion Sort", "Selection Sort"});
        tombolUrutkan = new JButton("Urutkan");
        panelMetode.setBackground(new Color(173, 216, 230));
        panelMetode.add(labelMetode);
        panelMetode.add(metodeComboBox);
        panelMetode.add(tombolUrutkan);

        labelStatus = new JLabel("Status: ");
        labelStatus.setFont(new Font("Arial", Font.BOLD, 16));
        labelStatus.setHorizontalAlignment(SwingConstants.CENTER);

        panelAtas.add(panelMasukan);
        panelAtas.add(panelMetode);

        panelPengurutan = new PanelPengurutan();
        panelPengurutan.setBackground(new Color(173, 216, 230));

        add(panelAtas, BorderLayout.NORTH);
        add(panelPengurutan, BorderLayout.CENTER);
        add(labelStatus, BorderLayout.SOUTH);

        tombolUrutkan.addActionListener(e -> mulaiPengurutan());
    }

    private void mulaiPengurutan() {
        String masukan = masukanField.getText();
        String[] angkaMasukan = masukan.split("\\s*,\\s*");
        try {
            data = Arrays.stream(angkaMasukan).mapToInt(Integer::parseInt).toArray();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Masukan tidak valid! Masukkan angka dipisahkan dengan koma.");
            return;
        }

        new Thread(() -> {
            String metode = (String) metodeComboBox.getSelectedItem();
            if ("Bubble Sort".equals(metode)) {
                bubbleSort(data);
            } else if ("Insertion Sort".equals(metode)) {
                insertionSort(data);
            } else if ("Selection Sort".equals(metode)) {
                selectionSort(data);
            }
        }).start();
    }

    private void bubbleSort(int[] data) {
        int n = data.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                labelStatus.setText("Status: Pengecekan ke-" + (i + 1));
                if (data[j] > data[j + 1]) {
                    int temp = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = temp;
                }
                panelPengurutan.perbaruiData(data, j, j + 1);
                tunggu(1000);
            }
        }
        labelStatus.setText("Status: Pengurutan selesai!");
        panelPengurutan.perbaruiData(data, -1, -1);
    }

    private void insertionSort(int[] data) {
        int n = data.length;
        for (int i = 1; i < n; i++) {
            int key = data[i];
            int j = i - 1;
            labelStatus.setText("Status: Pengecekan ke-" + i);
            while (j >= 0 && data[j] > key) {
                data[j + 1] = data[j];
                j--;
                panelPengurutan.perbaruiData(data, j + 1, i);
                tunggu(1000);
            }
            data[j + 1] = key;
            panelPengurutan.perbaruiData(data, -1, -1);
        }
        labelStatus.setText("Status: Pengurutan selesai!");
    }

    private void selectionSort(int[] data) {
        int n = data.length;
        for (int i = 0; i < n - 1; i++) {
            int minIdx = i;
            labelStatus.setText("Status: Pengecekan ke-" + (i + 1));
            for (int j = i + 1; j < n; j++) {
                if (data[j] < data[minIdx]) {
                    minIdx = j;
                }
                panelPengurutan.perbaruiData(data, i, minIdx);
                tunggu(1000);
            }
            int temp = data[minIdx];
            data[minIdx] = data[i];
            data[i] = temp;
            panelPengurutan.perbaruiData(data, -1, -1);
        }
        labelStatus.setText("Status: Pengurutan selesai!");
    }

    private void tunggu(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VisualisasiPengurutan app = new VisualisasiPengurutan();
            app.setVisible(true);
        });
    }

    class PanelPengurutan extends JPanel {
        private int[] data = {};
        private int sorot1 = -1, sorot2 = -1;

        public void perbaruiData(int[] data, int sorot1, int sorot2) {
            this.data = Arrays.copyOf(data, data.length);
            this.sorot1 = sorot1;
            this.sorot2 = sorot2;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (data == null || data.length == 0) return;

            int lebar = getWidth();
            int tinggi = getHeight();
            int lebarKotak = Math.min(lebar / data.length - 20, 120);
            int tinggiKotak = 100;
            int posX = (lebar - (data.length * (lebarKotak + 20))) / 2;
            int posY = tinggi / 2;

            for (int i = 0; i < data.length; i++) {
                int x = posX + i * (lebarKotak + 20);

                if (i == sorot1 || i == sorot2) {
                    g.setColor(Color.PINK);
                } else {
                    g.setColor(Color.WHITE);
                }

                g.fillRect(x, posY - tinggiKotak / 2, lebarKotak, tinggiKotak);
                g.setColor(Color.BLACK);
                g.drawRect(x, posY - tinggiKotak / 2, lebarKotak, tinggiKotak);

                g.setFont(new Font("Arial", Font.BOLD, 24));
                String text = String.valueOf(data[i]);
                FontMetrics metrics = g.getFontMetrics();
                int textWidth = metrics.stringWidth(text);
                int textHeight = metrics.getHeight();
                g.drawString(text, x + (lebarKotak - textWidth) / 2, posY + textHeight / 4);
            }
        }
    }
}
