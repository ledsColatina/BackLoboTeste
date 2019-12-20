package com.LoboProject.service;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;


@Service
public class PedidoService {

	//@Autowired
	//private PedidoRepository pedidoRepository;
	
	public void lerXML() {
		
		//Pedido pedido = new Pedido();
		
		try {
			
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse("C://Users/Sandro/Desktop/Pedido_0013251.xml");
			
			NodeList listaDePessoas = doc.getElementsByTagName("Inf");
            int tamanhoLista = listaDePessoas.getLength();
            
            for (int i = 0; i < tamanhoLista; i++) {
                
                // pego_cada_item_pessoa_como_um_nó_(node)
                Node noPessoa = listaDePessoas.item(i);
                // Verifica_se o noPessoa é do tipo element (e não do tipo texto etc)
                if(noPessoa.getNodeType() == Node.ELEMENT_NODE){
                    
                    Element elementoPessoa = (Element) noPessoa;
                 
                    String numero = elementoPessoa.getAttribute("Codigo"); 
                    //pedido.setNumero(Long.parseLong(elementoPessoa.getAttribute("Codigo")));
                    // pedido.setDataExpedicao();
                    // String Cliente = elementoPessoa.getAttribute("Cliente");
                    // pedido.setNomeCliente(elementoPessoa.getAttribute("Cliente"));
                    // pedido.setEndereco(elementoPessoa.getAttribute("Cidade")+elementoPessoa.getAttribute("Estado"));
                    // System.out.println("\n numero: %s" + pedido.getNomeCliente());
                    // imprimindo o id
                    System.out.println("NUMERO = " + numero);   
                 }
            }
            
           // pedidoRepository.save(pedido);
          /*  NodeList listaDeProdutos = doc.getElementsByTagName("Itens");
            int lista = listaDeProdutos.getLength();
            List<Produto> prod = new ArrayList();
            
            for (int i = 0; i < lista; i++) {
                
                // pego_cada_item_pessoa_como_um_nó_(node)
                Node noProduto = listaDeProdutos.item(i);
                // Verifica_se o noPessoa é do tipo element (e não do tipo texto etc)
                if(noProduto.getNodeType() == Node.ELEMENT_NODE){
                    
                    Element elementoProduto = (Element) noProduto;
                 
                    prod.add(elementoProduto.getAttribute("codProd"))
                    
                 }
            }*/
            
		} catch (SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	
	
		
	}
}