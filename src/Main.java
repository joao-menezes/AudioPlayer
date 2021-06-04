import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 * @author Joao Menezes
 */
public class Main extends JFrame{
    private static JFrame frame;
    private static JButton playButton,pauseButton;
    private static ImageIcon img_play,img_pause;
    private static JLabel music_nameLabel;
    private static JMenuBar bar;
    private static JMenu menu;
    private static JMenuItem open,exit;
    private static JFileChooser file;
    private static FileNameExtensionFilter fileNameExtensionFilter;
    public static File arquivo;
    private static Clip clip;

    public static void main(String[] args) throws Exception {
        JOptionPane.showMessageDialog(null, "Este programa suporta apenas formato wav", "Hello", JOptionPane.CLOSED_OPTION);
        /**manipulação de objetos */
        j_objects();
        /**customizando os componentes */
        custom();
        /***/
        add_components();    
        /**dimensao dos componentes */
        dimensions();
        /**acao dos botoes */
        btn_actions();

    }

    public static void j_objects(){
        frame = new JFrame("Audio Player");
        /**filtro de tipo de arquivo permitido */
        fileNameExtensionFilter = new FileNameExtensionFilter("Wav","wav");
        file = new JFileChooser();
        /**nome da janela de selecao de arquivo */
        file.setDialogTitle("Abrir");
        /**aplica o filtro */
        file.setFileFilter(fileNameExtensionFilter);
        bar = new JMenuBar();
        menu = new JMenu("Menu");
        open = new JMenuItem("Open");
        /**acao do teclado relacionado a abrir */
        open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));
        exit = new JMenuItem("Exit");
        /**acao do teclado relacionado a sair */
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));
        img_play = new ImageIcon("src/icons/play.png");
        img_pause = new ImageIcon("src/icons/pause.png");
        /**imagem dos botoes */
        playButton = new JButton(img_play);
        pauseButton = new JButton(img_pause);
        music_nameLabel = new JLabel("-");
    }

    public static void custom() {
        frame.setJMenuBar(bar);
        /**os objetos possuem um tamanho escolhido ao inves do tamanho do frame */
        frame.setLayout(null);
        /**sai de tudo ao fechar o programa */
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(300,200);
        /**centraliza a janela */
        frame.setLocationRelativeTo(null);
        /**nao deixa o usuer redimensionar a nela */
        frame.setResizable(false);
        frame.setVisible(true);

        playButton.setFocusable(false);
        playButton.setContentAreaFilled(false);
        playButton.setBorderPainted(false);
        playButton.setBorder(null);

        pauseButton.setVisible(false);
        pauseButton.setFocusable(false);
        pauseButton.setContentAreaFilled(false);
        pauseButton.setBorderPainted(false);
        pauseButton.setBorder(null);
    }
   
    private static void add_components() {
        bar.add(menu);
        menu.add(open);
        menu.add(exit);
        frame.add(playButton);
        frame.add(pauseButton);
        frame.add(music_nameLabel);
    }

    public static void dimensions() {
        playButton.setBounds(90,15,100,100);
        pauseButton.setBounds(90,20,100,100);
        music_nameLabel.setBounds(10,110,500,20);
    }
    
    public static void btn_actions() {
    
    playButton.addActionListener(new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                PlayAudio();
                clip.start();
                playButton.setVisible(false);
                pauseButton.setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Escolha uma musica", "Error", JOptionPane.ERROR_MESSAGE);
                try {
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                } catch (InterruptedException | IOException e1) {
                    e1.printStackTrace();
                }
            }
            
        }
        
    });

    pauseButton.addActionListener(new ActionListener(){

        @Override
        public void actionPerformed(ActionEvent e) {
            arquivo = file.getSelectedFile();
            clip.stop();
            playButton.setVisible(true);
            pauseButton.setVisible(false);
        } 
    });

    open.addActionListener(new ActionListener() {

        public void actionPerformed(ActionEvent e) {

             file.setFileSelectionMode(JFileChooser.FILES_ONLY);
             int i = file.showSaveDialog(null);

             if (!file.getSelectedFile().getName().contains(".wav")) {
                JOptionPane.showMessageDialog(null, "Error", "Error", JOptionPane.ERROR_MESSAGE);  
             }else{
                if (i  == JFileChooser.APPROVE_OPTION){
                    arquivo = file.getSelectedFile();
                    music_nameLabel.setText(arquivo.getName());
                }else{ }  
             }
        }
      });

    exit.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          System.exit(0);
        }
      });
}

public static void PlayAudio() {

    try {
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(arquivo.getPath()).getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY); //Para repetir o som.
    } catch (Exception ex) {
        System.out.println("Erro ao executar SOM!");
        ex.printStackTrace();
    }

}

}