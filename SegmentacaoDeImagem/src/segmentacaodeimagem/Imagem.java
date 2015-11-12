package segmentacaodeimagem;

import br.ufrn.imd.lp2.imagesegmentation.ImageInformation;
import br.ufrn.imd.lp2.imagesegmentation.ImageSegmentation;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


/**
 * Uma classe que realiza a segmentação de uma imagem, bem como o mapa de rótulos da mesma.
 * @author Hiago Miguel & Rai Vitor.
 */
public class Imagem {      
    private static int[] variacaoGray;
    /* Define a intensidade da tonalidade cinza inicial a ser utilizada*/
    private static int defGrey;

    private static ArrayList<Integer> pixelRegion, pixelsDaImagemSegmentadaBckp;
    ImageInformation seg;
    
    /**
     * Segmenta uma dada imagem de acordo com os parâmetros abaixo.
     * 
     * @param path Caminho da imagem.
     * @param blur Nível de desfoque (blur) para suavizar arestas da imagem.
     * @param radius Principal parâmetro do algoritmo Mean Shift. Sua mudança é bastante sensível ao resultado da segmentação.
     * @param size Tamanho mínimo das regiões obtidas na segmentação.
     */
    public void segmentar(String path, double blur, int radius, int size) {                        
        seg = ImageSegmentation.performSegmentation(path, blur,radius,size);
        variacaoGray = new int[seg.getTotalRegions()];        
        defGrey = 255/seg.getTotalRegions();
        setPixelsDaImagemSegmentadaBckp(new ArrayList<>());
        CopiarArray();
        setPixelRegion(new ArrayList<>());
    }           
    
    /**
     * Realiza uma cópia da imagem segmentada.
     */
    private void CopiarArray(){
        for(int i=0; i<getPixelsDaImagemSegmentada().length; i++){
            getPixelsDaImagemSegmentadaBckp().add(getPixelsDaImagemSegmentada()[i]);
        }
    }
    
    /**
     * Restaura a imagem segmentada com o brilho original dela
     * @param flag se ela for 1 limpa 'pixelRegion'
     */
    public void RestaurarImg(int flag){
        for (int i = 0; i < getPixelsDaImagemSegmentada().length; i++) {
            getPixelsDaImagemSegmentada()[i] = getPixelsDaImagemSegmentadaBckp().get(i);
        }
        if(flag == 1){
            getPixelRegion().clear();
        }
    }
    
    /**
     * Cria o mapa de rótulos de uma dada imagem segmentada.
     * 
     * A imagem é construída a partir da informação das regiões obtidas através do mapa de rótulos 
     * (método getSegmentedImageMap) e do número total de regiões (método getTotalRegions).
     * 
    */
    public void GerarMapaRotulos() {        
        /* Recebe a tonalidade cinza de uma regiao.*/
        int gray = 0;
        /* Pixel RGB de tonalidade cinza a ser mapeado para o array pixelsDaImagemSegmentada. */
        int rgb = 0;        
        
        GapGray(getTotalRegioes());
        
        for(int i = 0; i < getPixelsDaImagemSegmentada().length; i++) {
            gray = variacaoGray[getMapaDaRegiaoSegmentada()[i]];
            rgb = ((gray&0x0ff)<<16)|((gray&0x0ff)<<8)|(gray&0x0ff);
            getPixelsDaImagemSegmentada()[i] = rgb;            
        }        
    }        
    
    /**
     * Gera espacamentos iguais entre os possiveis 255 cinzas
     * @param tamanhoRegiao - Quantidade de regioes existentes na imagem segmentada 
     */
    private static void GapGray(int tamanhoRegiao){
        for(int i = 0; i < tamanhoRegiao; i++) {
            variacaoGray[i] = defGrey*i;
        }
    }

    /**
     * @return the mapaDaRegiaoSegmentada
     */
    public int[] getMapaDaRegiaoSegmentada() {
        return seg.getSegmentedImageMap();
    }

    /**
     * @return the pixelsDaImagemSegmentada
     */
    public int[] getPixelsDaImagemSegmentada() {
        return seg.getRegionMarkedPixels();
    }


    /**
     * @return the pixelRegion
     */
    public ArrayList<Integer> getPixelRegion() {
        return pixelRegion;
    }

    /**
     * @param aPixelRegion the pixelRegion to set
     */
    public void setPixelRegion(ArrayList<Integer> aPixelRegion) {
        pixelRegion = aPixelRegion;
    }

    /**
     * @return the pixelsDaImagemSegmentadaBckp
     */
    public ArrayList<Integer> getPixelsDaImagemSegmentadaBckp() {
        return pixelsDaImagemSegmentadaBckp;
    }

    /**
     * @param aPixelsDaImagemSegmentadaBckp the pixelsDaImagemSegmentadaBckp to set
     */
    public void setPixelsDaImagemSegmentadaBckp(ArrayList<Integer> aPixelsDaImagemSegmentadaBckp) {
        pixelsDaImagemSegmentadaBckp = aPixelsDaImagemSegmentadaBckp;
    }

    /**
     * @return the imgSegmentada
     */
    public BufferedImage getImgSegmentada() {
        return seg.getRegionMarkedImage();
    }

    /**
     * @return the totalRegioes
     */
    public int getTotalRegioes() {
        return seg.getTotalRegions();
    }
}