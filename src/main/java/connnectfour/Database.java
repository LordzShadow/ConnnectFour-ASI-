package connnectfour;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Database {

    private static NodeList games;
    private static String xml = "database.xml";

    // Get the games from the xml file
    public static ArrayList<String[]> readXML() {
        Document dom;
        ArrayList<String[]> game_list = new ArrayList<>();
        // Make an  instance of the DocumentBuilderFactory
        try {
            dom = getDOM();
            Element doc = dom.getDocumentElement();
            removeNodes(doc);
            games = doc.getElementsByTagName("game");
            for (int i = 0; i < games.getLength(); i++) {
                Node game = games.item(i);
                game_list.add(getValues(game));
            }
            return game_list;

        } catch (Exception ioe) {
            System.err.println(ioe.getMessage());
        }

        return null;
    }

    private static Document getDOM() throws ParserConfigurationException, IOException, SAXException {
        Document dom;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        try {
            dom = db.parse(xml);
        } catch (FileNotFoundException e) {
            try {
                e.printStackTrace();
                sendToXml(db.newDocument());
            } catch (TransformerException transformerException) {
                transformerException.printStackTrace();
            }
        }
        dom = db.parse(xml);
        return dom;
    }

    // Save a game to the xml file.
    public static void saveToXML(String[] game) {
        Document dom;
        Element e;

        try {
            dom = getDOM();

            // get the root element
            Element rootEle = dom.getDocumentElement();
            removeNodes(rootEle);

            // Create a new game element and add children.
            e = dom.createElement("game");
            Element type = dom.createElement("type");
            type.appendChild(dom.createTextNode(game[0]));
            Element size = dom.createElement("board-size");
            size.appendChild(dom.createTextNode(game[1]));
            Element moves = dom.createElement("moves");
            moves.appendChild(dom.createTextNode(game[2]));
            Element winner = dom.createElement("winner");
            winner.appendChild(dom.createTextNode(game[3]));
            e.appendChild(type);
            e.appendChild(size);
            e.appendChild(moves);
            e.appendChild(winner);
            rootEle.appendChild(e);

            try {
                // Properties for the xml file.
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "games.dtd");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                // send DOM to file
                tr.transform(new DOMSource(dom),
                        new StreamResult(new FileOutputStream(xml)));

            } catch (TransformerException | IOException te) {
                System.out.println(te.getMessage());
            }
        } catch (ParserConfigurationException pce) {
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
        } catch (SAXException | IOException saxException) {
            saxException.printStackTrace();
        }
    }

    private static void sendToXml(Document dom) throws TransformerException, IOException {
        String[] game = new String[]{"--", "--", "--", "--"};
        Element rootE = dom.createElement("games");
        Element e = dom.createElement("game");
        Element type = dom.createElement("type");
        type.appendChild(dom.createTextNode(game[0]));
        Element size = dom.createElement("board-size");
        size.appendChild(dom.createTextNode(game[1]));
        Element moves = dom.createElement("moves");
        moves.appendChild(dom.createTextNode(game[2]));
        Element winner = dom.createElement("winner");
        winner.appendChild(dom.createTextNode(game[3]));
        e.appendChild(type);
        e.appendChild(size);
        e.appendChild(moves);
        e.appendChild(winner);
        rootE.appendChild(e);
        dom.appendChild(rootE);
        Transformer tr = TransformerFactory.newInstance().newTransformer();
        tr.setOutputProperty(OutputKeys.INDENT, "yes");
        tr.setOutputProperty(OutputKeys.METHOD, "xml");
        tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "games.dtd");
        tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        // send DOM to file
        tr.transform(new DOMSource(dom),
                new StreamResult(new FileOutputStream(xml)));

        BufferedWriter bw = new BufferedWriter(new FileWriter(new File("games.dtd")));
        bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "        <!ELEMENT games ANY>\n" +
                "        <!ELEMENT game (type, board-size, moves, winner)>\n" +
                "        <!ELEMENT type (#PCDATA)>\n" +
                "        <!ELEMENT board-size (#PCDATA)>\n" +
                "        <!ELEMENT moves (#PCDATA)>\n" +
                "        <!ELEMENT winner (#PCDATA)>");
        bw.close();
    }

    // Get the values of the game node.
    private static String[] getValues(Node game) {
        String[] values = new String[4];
        NodeList data = game.getChildNodes();
        for (int i = 0; i < data.getLength(); i++) {
            values[i] = data.item(i).getTextContent().strip();
        }
        return values;
    }

    // Remove nodes, that are empty.
    public static void removeNodes(Node node) {

        NodeList list = node.getChildNodes();
        List<Node> nodesToRecursivelyCall = new LinkedList<>();

        // Put child nodes in a different list to recursively call them.
        // (Required because if you remove a node it's siblings are automagically moved to the start of the list)
        for (int i = 0; i < list.getLength(); i++) {
            nodesToRecursivelyCall.add(list.item(i));
        }
        // Call same function on child nodes
        for(Node tempNode : nodesToRecursivelyCall) {
            removeNodes(tempNode);
        }

        // If is an empty element or text node, remove it.
        boolean emptyElement = node.getNodeType() == Node.ELEMENT_NODE
                && node.getChildNodes().getLength() == 0;
        boolean emptyText = node.getNodeType() == Node.TEXT_NODE
                && node.getNodeValue().trim().isEmpty();

        if (emptyElement || emptyText) {
            if(!node.hasAttributes()) {
                node.getParentNode().removeChild(node);
            }
        }

    }
}
