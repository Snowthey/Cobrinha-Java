import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.util.Optional;

public class Cobrinha {

    public static char direcao = 'D';
    private int TamanhoDaCobra;
    public int QuantidadeComida;
    private final int[] eixoX = new int[Jogo.UNIDADES];
    private final int[] eixoY = new int[Jogo.UNIDADES];

    public Cobrinha(){
        TamanhoDaCobra = 6;
        QuantidadeComida = 0;
    }

    public void Desenhar(Graphics g){
        for(int i = 0; i < TamanhoDaCobra; i++){
            if(i == 0){
                g.setColor(Color.black);
                g.fillRect(eixoX[0], eixoY[0], Jogo.TAMANHO_BLOCO, Jogo.TAMANHO_BLOCO);
            } else {
                g.setColor(new Color(45, 180, 0));
                g.fillRect(eixoX[i], eixoY[i], Jogo.TAMANHO_BLOCO, Jogo.TAMANHO_BLOCO);
            }
        }
    }

    public void andar() throws InterruptedException{
        for(int i = TamanhoDaCobra; i > 0; i--){
            eixoX[i] = eixoX[i - 1];
            eixoY[i] = eixoY[i - 1];
        }

        Jogo.Mutex.acquire();

        switch (direcao){
            case 'C':
                eixoY[0] = eixoY[0] - Jogo.TAMANHO_BLOCO;
                break;
            case 'B':
                eixoY[0] = eixoY[0] + Jogo.TAMANHO_BLOCO;
                break;
            case 'E':
                eixoX[0] = eixoX[0] - Jogo.TAMANHO_BLOCO;
                break;
            case 'D':
                eixoX[0] = eixoX[0] + Jogo.TAMANHO_BLOCO;
                break;
            default:
                break;
        }

        Jogo.Mutex.release();
    }

    public void TocarEfeito() throws LineUnavailableException, IOException, UnsupportedAudioFileException{
        File arquivo = new File("C:/Users/sherl/IdeaProjects/Cobrinha/src/cute.wav");
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(arquivo);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    }

    public boolean alcancouComida() throws LineUnavailableException, IOException, UnsupportedAudioFileException{

        if(eixoX[0] == Comida.posicao_x && eixoY[0] == Comida.posicao_y) {
            TamanhoDaCobra++;
            QuantidadeComida++;

            TocarEfeito();
            return true;
        } else {
            return false;
        }
    }

    public boolean VerificarGameOver(){
        boolean perdeu = false;

        for(int i = TamanhoDaCobra; i > 0; i--){
            if( (eixoX[0] == eixoX[i]) && (eixoY[0] == eixoY[i])){
                perdeu = true;
                break;
            }
        }

        if((eixoX[0] < 0) || (eixoX[0] > Jogo.LARGURA_TELA)){
            perdeu = true;
        }

        if((eixoY[0] < 0) || (eixoY[0] > Jogo.ALTURA_TELA)){
            perdeu = true;
        }

        return perdeu;
    }
}
