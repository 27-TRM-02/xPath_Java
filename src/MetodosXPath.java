
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author trm
 */
public class MetodosXPath {

    // Estructura DOM
    Document doc;

    public int abrir_XML_XPATH(File fichero) {
        // Almacenamos estructura DOM
        doc = null;
        try {
            // Se crea un objeto DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Indica que el modelo DOM no debe contemplar los comentarios 
            // que tenga el XML.
            factory.setIgnoringComments(true);
            // Ignora los espacios en blanco que tenga el documento
            factory.setIgnoringElementContentWhitespace(true);
            // Creamos objeto para cargar la estructura del arbol del DOM
            DocumentBuilder builder = factory.newDocumentBuilder();
            // Parsea el documento XML y genera el DOM equivalente
            doc = builder.parse(fichero);
            // Nos apunta al arbol DOM listo para ser recorrido 
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * 
     * @param consultaXPath String con la consulta XPath
     * @return String salida con el resultado de la consulta XPath
     */
    public String EjecutaXPath(String consultaXPath) {
        String salida = "";
        try {
            // Se crea Objeto XPath
            javax.xml.xpath.XPath xpath = XPathFactory.newInstance().newXPath();
            // Creamos una XPathExpression con la consulta deseada
            XPathExpression exp = xpath.compile(consultaXPath);
            // Ejecuta la consulta indicando que se ejecuta sobre el DOM
            // Devolverá el resultado como una lista de nodos
            Object result = exp.evaluate(doc, XPathConstants.NODESET);
            NodeList nodelist = (NodeList) result;
            // Recorremos la lista del resultado de la consulta
            for (int i = 0; i < nodelist.getLength(); i++) {
                // Si la consulta muestra la informacion entera de un libro
                if (nodelist.item(i).getNodeName().equals("Libro")) {
                    String[] datosNodoLibro = procesarLibro(nodelist.item(i));
                    salida += "\n" + "Publicado en: " + datosNodoLibro[0];
                    salida += "\n" + "El Titulo es: " + datosNodoLibro[1];
                    salida += "\n" + "El Autor es: " + datosNodoLibro[2];
                    salida += "\n --------------------------------";
                } else {
                    // La consulta es sobre un dato 
                    salida += "\n" + nodelist.item(i).getFirstChild().getNodeValue();
                }
            }
            // Todo ha ido bien y se devuelve String con el resultado
            return salida;
        } catch (XPathExpressionException | DOMException ex) {
            System.err.println("Error: " + ex.toString());
            return null;
        }
    }

    /**
     * @param n Nodo Libro
     * @return Array de Strings con los Datos del Libro pasado
     */
    private String[] procesarLibro(Node n) {
        try {
            String datos[] = new String[3];
            Node ntemp = null;
            int contador = 1;
            //Obtiene el valor del primer atrb del nodo // Como solo hay un atrb en "Libros" no lo metemos en un bucle for
            datos[0] = n.getAttributes().item(0).getNodeValue();
            //Obtiene los hijos del "Libro" - "Titulo y Autor"
            NodeList libro = n.getChildNodes();

            for (int i = 0; i < libro.getLength(); i++) {
                ntemp = libro.item(i);
                if (ntemp.getNodeType() == Node.ELEMENT_NODE) {
                    //Se accede al nodo TEXT hijo de ntemp y saca su valor para obtener el texto con "Titulo y Autor"
                    datos[contador] = ntemp.getFirstChild().getNodeValue();
                    contador++;
                }
            }
            return datos;
        } catch (Exception e) {
            System.err.println(e.toString());
            return null;
        }
    }

}
