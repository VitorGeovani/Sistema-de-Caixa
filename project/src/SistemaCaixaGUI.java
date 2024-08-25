import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SistemaCaixaGUI {
    private JFrame frame;
    private JTextField precoField;
    private JTextArea resumoArea;
    private JComboBox<String> metodoPagamentoCombo;
    private Caixa caixa;
    private double troco;

    public SistemaCaixaGUI() {
        caixa = new Caixa();
        frame = new JFrame("Sistema de Caixa do Supermercado");
        frame.setSize(500, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout(10, 10));
        frame.getContentPane().setBackground(new Color(245, 245, 245));

        // Centralizar a janela na tela
        frame.setLocationRelativeTo(null);

        // Estilo das fontes
        Font fontLabel = new Font("Arial", Font.BOLD, 14);
        Font fontField = new Font("Arial", Font.PLAIN, 14);
        Font fontButton = new Font("Arial", Font.BOLD, 16);

        // Painel de entrada de dados
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Preço do item
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel precoLabel = new JLabel("Preço do item (R$):");
        precoLabel.setFont(fontLabel);
        inputPanel.add(precoLabel, gbc);

        precoField = new JTextField(10);
        precoField.setFont(fontField);
        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(precoField, gbc);

        // Método de pagamento
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel metodoLabel = new JLabel("Método de pagamento:");
        metodoLabel.setFont(fontLabel);
        inputPanel.add(metodoLabel, gbc);

        metodoPagamentoCombo = new JComboBox<>(new String[]{"Dinheiro", "Cartão", "Outro"});
        metodoPagamentoCombo.setFont(fontField);
        gbc.gridx = 1;
        gbc.gridy = 1;
        inputPanel.add(metodoPagamentoCombo, gbc);

        // Adicionar Item
        JButton addButton = new JButton("Adicionar Item");
        addButton.setFont(fontButton);
        addButton.setBackground(new Color(60, 179, 113));
        addButton.setForeground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(addButton, gbc);

        // Adicionar item ao pressionar Enter
        precoField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    adicionarItem();
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarItem();
            }
        });

        frame.add(inputPanel, BorderLayout.NORTH);

        // Área de resumo
        resumoArea = new JTextArea();
        resumoArea.setEditable(false);
        resumoArea.setFont(new Font("Courier New", Font.PLAIN, 16));
        resumoArea.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        JScrollPane scrollPane = new JScrollPane(resumoArea);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Painel de botões
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        JButton resumoButton = new JButton("Exibir Resumo");
        resumoButton.setFont(fontButton);
        resumoButton.setBackground(new Color(70, 130, 180));
        resumoButton.setForeground(Color.WHITE);
        buttonPanel.add(resumoButton);

        JButton abrirCaixaButton = new JButton("Abrir Caixa");
        abrirCaixaButton.setFont(fontButton);
        abrirCaixaButton.setBackground(new Color(220, 20, 60));
        abrirCaixaButton.setForeground(Color.WHITE);
        buttonPanel.add(abrirCaixaButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Actions dos botões
        resumoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exibirResumo();
            }
        });

        abrirCaixaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirCaixa();
            }
        });

        frame.setVisible(true);
    }

    private void adicionarItem() {
        String precoTexto = precoField.getText().trim();

        if (precoTexto.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "O preço do item não pode estar vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double preco = Double.parseDouble(precoTexto);
            if (preco <= 0) {
                JOptionPane.showMessageDialog(frame, "O preço deve ser um número positivo.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            caixa.adicionarItem(new Item("Item", preco));
            precoField.setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Preço inválido. Insira um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exibirResumo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Itens no caixa:\n");
        for (Item item : caixa.getItens()) {
            sb.append("- ").append(item.getNome()).append(": R$").append(String.format("%.2f", item.getPreco())).append("\n");
        }
        sb.append("Total a pagar: R$").append(String.format("%.2f", caixa.calcularTotal())).append("\n");

        // Se o método de pagamento for "Dinheiro", solicitar valor pago e calcular troco
        if (metodoPagamentoCombo.getSelectedItem().equals("Dinheiro")) {
            String valorPagoTexto = JOptionPane.showInputDialog(frame, "Digite o valor pago:");
            try {
                double valorPago = Double.parseDouble(valorPagoTexto);
                if (valorPago < caixa.calcularTotal()) {
                    JOptionPane.showMessageDialog(frame, "O valor pago é menor que o total. Insira um valor maior ou igual ao total.", "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                troco = caixa.calcularTroco(valorPago);
                sb.append("Troco a devolver: R$").append(String.format("%.2f", troco)).append("\n");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Valor pago inválido. Insira um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }

        resumoArea.setText(sb.toString());
    }

    private void abrirCaixa() {
        // Apenas exibir o troco calculado anteriormente (se houver)
        if (metodoPagamentoCombo.getSelectedItem().equals("Dinheiro") && troco > 0) {
            JOptionPane.showMessageDialog(frame, "O caixa está agora aberto. Pegue o dinheiro!\nTroco a devolver: R$" + String.format("%.2f", troco), "Caixa Aberto", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "O caixa está agora aberto.", "Caixa Aberto", JOptionPane.INFORMATION_MESSAGE);
        }
        
        caixa.limparItens();  // Limpar a lista de itens após abrir o caixa
        resumoArea.setText("");  // Limpa a área de resumo
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SistemaCaixaGUI();
            }
        });
    }
}