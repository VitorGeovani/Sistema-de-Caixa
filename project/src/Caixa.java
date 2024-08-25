import java.util.ArrayList;
import java.util.List;

public class Caixa {
    private List<Item> itens;
    private double total;

    public Caixa() {
        itens = new ArrayList<>();
        total = 0.0;
    }

    public void adicionarItem(Item item) {
        itens.add(item);
        total += item.getPreco();
    }

    public double calcularTotal() {
        return total;
    }

    public List<Item> getItens() {
        return itens;
    }

    public void abrirCaixa() {
        System.out.println("O caixa está agora aberto. Pegue o dinheiro!");
        // Lógica para abrir o caixa usando hardware específico.
    }

    public double calcularTroco(double valorPago) {
        return valorPago - total;
    }

    public void limparItens() {
        itens.clear();
        total = 0.0;
    }
}