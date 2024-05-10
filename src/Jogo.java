import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.awt.event.KeyListener;

public class Jogo  extends JPanel implements Runnable {
    public static final int LARGURA_TELA = 1300;
    public static final int ALTURA_TELA = 750;
    public static final int TAMANHO_BLOCO = 30;
    public static final int UNIDADES = LARGURA_TELA * ALTURA_TELA / (TAMANHO_BLOCO * TAMANHO_BLOCO);
    public static final int INTERVALO = 200;
    public static final String NOME_FONTE = "Ink Free";
    public boolean GameOver = false;
    Cobrinha objCobrinha;
    Comida objComida;
    Random random;
    public static Semaphore Mutex;

    public Jogo(){
        setPreferredSize(new Dimension(LARGURA_TELA, ALTURA_TELA));
        setBackground(Color.WHITE);
        setFocusable(true);

        addKeyListener(new InterrupcaoTeclado());
        objCobrinha = new Cobrinha();
        objComida =  new Comida();
        objComida.CriarNovaPosicao();
        GameOver = false;
        Mutex = new Semaphore(1);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);

        try{
            desenharTela(g);
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e){
            e.printStackTrace();
        }
    }

    public void desenharTela(Graphics g) throws LineUnavailableException, IOException, UnsupportedAudioFileException{
        if(GameOver == false){
            objComida.Desenhar(g);
            objCobrinha.Desenhar(g);
            g.setColor(Color.red);
            g.setFont(new Font(NOME_FONTE, Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            String texto = "Pontos: " + objCobrinha.QuantidadeComida;
            g.drawString(texto, (LARGURA_TELA - metrics.stringWidth(texto)) / 2, g.getFont().getSize());
        } else{
            fimDeJogo(g);
        }
    }

    public void fimDeJogo(Graphics g) throws LineUnavailableException, IOException, UnsupportedAudioFileException{

        g.setColor(Color.red);
        g.setFont(new Font(NOME_FONTE, Font.BOLD, 75));
        FontMetrics fonteFinal = getFontMetrics(g.getFont());
        g.drawString("Fim do Jogo.", (LARGURA_TELA - fonteFinal.stringWidth("Fim de Jogo")) / 2, ALTURA_TELA / 2);

        File arquivo =  new File("C:/Users/sherl/IdeaProjects/Cobrinha/src/erro.wav");
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(arquivo);
        Clip clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
    }

    public void run(){
        while(GameOver == false){
            try{
                objCobrinha.andar();
            } catch (InterruptedException e1){
                e1.printStackTrace();
            }

            try{
                if(objCobrinha.alcancouComida() == true){
                    objComida.CriarNovaPosicao();
                }
            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e){
                e.printStackTrace();
            }

            GameOver = objCobrinha.VerificarGameOver();
            repaint();

            try{
                Thread.sleep(100);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

}
