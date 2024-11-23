package objects;

import pt.iscte.poo.utils.Point2D;

public class Wall extends GameElement {

    // Construtor para inicializar a posição e o nome da imagem
    public Wall(Point2D position) {
        super(position, "Wall", 1); // "wall" deve corresponder ao nome da imagem do sprite
    }

    // Sobrescreve o método getPosition para adicionar verificação de nulo
    @Override
    public Point2D getPosition() {
        if (super.getPosition() == null) {
            System.err.println("Erro: posição nula para o elemento Wall.");
        }
        return super.getPosition();
    }

    // Sobrescreve o método getName (opcional, já é definido na classe base)
    @Override
    public String getName() {
        return "Wall";
    }
}


	
