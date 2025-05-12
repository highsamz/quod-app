import org.opencv.core.Core;

public class TesteOpenCV {

    static {
        try {
            String caminhoDll = new java.io.File("src/main/resources/libs/opencv_java4110.dll").getAbsolutePath();
            System.load(caminhoDll);
            System.out.println("OpenCV DLL carregada com sucesso: " + caminhoDll);
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Erro ao carregar a biblioteca nativa do OpenCV: " + e.getMessage());
            throw new RuntimeException("Falha ao carregar a DLL do OpenCV", e);
        }
    }


    public static void main(String[] args) {


        try {

            System.load(new java.io.File("src/main/resources/libs/opencv_java4110.dll").getAbsolutePath());
            System.out.println("OpenCV DLL carregada com sucesso.");
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Erro ao carregar a biblioteca nativa do OpenCV: " + e.getMessage());
        }
    }
}
