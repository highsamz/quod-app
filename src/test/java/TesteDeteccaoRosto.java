import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class TesteDeteccaoRosto {

    public static void main(String[] args) {
        // Carrega a biblioteca OpenCV
        System.load("C:\\Users\\samue\\trabalhos-faculdade\\quod-app\\libs\\opencv_java470.dll");

        // Caminho da imagem para teste
        String caminhoImagem = "src/main/resources/imagensTeste/rosto-desenhado.jpg"; // Substitua pelo caminho correto da sua imagem
        String classificadorCaminho = "src/main/resources/classifiers/haarcascade_frontalface_default.xml"; // Caminho do classificador Haar

        // Tente detectar rostos
        try {
            // Carrega a imagem
            Mat imagem = Imgcodecs.imread(caminhoImagem);
            if (imagem.empty()) {
                System.out.println("Erro ao carregar a imagem.");
                return;
            }

            // Converte para tons de cinza
            Mat imagemCinza = new Mat();
            Imgproc.cvtColor(imagem, imagemCinza, Imgproc.COLOR_BGR2GRAY);

            // Carrega o classificador Haar
            CascadeClassifier classificador = new CascadeClassifier(classificadorCaminho);
            if (classificador.empty()) {
                System.out.println("Erro ao carregar o classificador.");
                return;
            }

            // Detecta rostos na imagem
            MatOfRect rostosDetectados = new MatOfRect();
            classificador.detectMultiScale(imagemCinza, rostosDetectados);

            // Exibe o número de rostos detectados
            System.out.println("Rostos detectados: " + rostosDetectados.toArray().length);

            // Se houver rostos detectados, desenha um retângulo em torno de cada rosto
            for (org.opencv.core.Rect rosto : rostosDetectados.toArray()) {
                Imgproc.rectangle(imagem, rosto.tl(), rosto.br(), new org.opencv.core.Scalar(0, 255, 0), 2);
            }

            // Salva a imagem com os rostos detectados
            Imgcodecs.imwrite("imagem_com_rostos_detectados.jpg", imagem);

            System.out.println("Imagem com rostos detectados salva como 'imagem_com_rostos_detectados.jpg'.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
