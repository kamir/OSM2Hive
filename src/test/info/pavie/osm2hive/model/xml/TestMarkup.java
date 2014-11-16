package info.pavie.osm2hive.model.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link Markup}.
 * @author Adrien PAVIE
 */
public class TestMarkup {
//ATTRIBUTES
	private Markup m1;
	
//SETUP
	@Before
	public void setUp() throws Exception {
	}

//TESTS
/*
 * Constructor
 */
	@Test
	public void testMarkupStartSimple() throws InvalidMarkupException {
		m1 = new Markup("<node>");
		assertEquals(Markup.START, m1.getType());
		assertEquals("", m1.getText());
		assertEquals("node", m1.getName());
	}
	
	@Test(expected=InvalidMarkupException.class)
	public void testMarkupStartInvalid() throws InvalidMarkupException {
		m1 = new Markup("<no de>");
	}
	
	@Test
	public void testMarkupEmptySimple() throws InvalidMarkupException {
		m1 = new Markup("<node/>");
		assertEquals(Markup.EMPTY, m1.getType());
		assertEquals("", m1.getText());
		assertEquals("node", m1.getName());
	}
	
	@Test(expected=InvalidMarkupException.class)
	public void testMarkupEmptySimpleInvalid() throws InvalidMarkupException {
		m1 = new Markup("<node/ >");
	}
	
	@Test
	public void testMarkupEmptySimpleSpaced() throws InvalidMarkupException {
		m1 = new Markup("<node />");
		assertEquals(Markup.EMPTY, m1.getType());
		assertEquals("", m1.getText());
		assertEquals("node", m1.getName());
	}
	
	@Test
	public void testMarkupEndSimple() throws InvalidMarkupException {
		m1 = new Markup("</node>");
		assertEquals(Markup.END, m1.getType());
		assertEquals("", m1.getText());
		assertEquals("node", m1.getName());
	}
	
	@Test(expected=InvalidMarkupException.class)
	public void testMarkupEndSimpleInvalid() throws InvalidMarkupException {
		m1 = new Markup("</ node>");
	}
	
	@Test
	public void testMarkupEndSimpleWhitespaces() throws InvalidMarkupException {
		m1 = new Markup("   </node>  \t");
		assertEquals(Markup.END, m1.getType());
		assertEquals("", m1.getText());
		assertEquals("node", m1.getName());
	}
	
	@Test
	public void testMarkupEmptyAttributeSpaced() throws InvalidMarkupException {
		m1 = new Markup("<node key=\"value\" />");
		assertEquals(Markup.EMPTY, m1.getType());
		assertEquals("", m1.getText());
		assertEquals("value", m1.getAttribute("key"));
		assertTrue(m1.hasAttribute("key"));
		assertEquals("node", m1.getName());
	}
	
	@Test
	public void testMarkupEmptyAttributeWithpunctationSpaced() throws InvalidMarkupException {
		m1 = new Markup("<node key=\"value, is it the key !?\" />");
		assertEquals(Markup.EMPTY, m1.getType());
		assertEquals("", m1.getText());
		assertEquals("value, is it the key !?", m1.getAttribute("key"));
		assertTrue(m1.hasAttribute("key"));
		assertEquals("node", m1.getName());
	}
	
	@Test(expected=InvalidMarkupException.class)
	public void testMarkupEmptyAttributeSpacedInvalid() throws InvalidMarkupException {
		m1 = new Markup("<node key = \"value\" />");
	}
	
	@Test(expected=InvalidMarkupException.class)
	public void testMarkupEmptyAttributeSpacedInvalidChar() throws InvalidMarkupException {
		m1 = new Markup("<node key=\"val\'ue\" />");
	}
	
	@Test
	public void testMarkupEmptyMultiAttributesSpaced() throws InvalidMarkupException {
		m1 = new Markup("<node key=\"value\" key2=\"lala2\" life=\"42\" />");
		assertEquals(Markup.EMPTY, m1.getType());
		assertEquals("", m1.getText());
		assertEquals("value", m1.getAttribute("key"));
		assertTrue(m1.hasAttribute("key"));
		assertEquals("lala2", m1.getAttribute("key2"));
		assertTrue(m1.hasAttribute("key2"));
		assertEquals("42", m1.getAttribute("life"));
		assertTrue(m1.hasAttribute("life"));
		assertEquals("node", m1.getName());
	}
	
	@Test
	public void testMarkupEmptyMultiAttributesUnspaced() throws InvalidMarkupException {
		m1 = new Markup("<node key=\"value\" key2=\"lala2\" life=\"42\"/>");
		assertEquals(Markup.EMPTY, m1.getType());
		assertEquals("", m1.getText());
		assertEquals("value", m1.getAttribute("key"));
		assertTrue(m1.hasAttribute("key"));
		assertEquals("lala2", m1.getAttribute("key2"));
		assertTrue(m1.hasAttribute("key2"));
		assertEquals("42", m1.getAttribute("life"));
		assertTrue(m1.hasAttribute("life"));
		assertEquals("node", m1.getName());
	}
	
	@Test
	public void testMarkupStartMultiAttributesUnspaced() throws InvalidMarkupException {
		m1 = new Markup("<node key=\"value\" key2=\"lala2\" life=\"42\">");
		assertEquals(Markup.START, m1.getType());
		assertEquals("", m1.getText());
		assertEquals("value", m1.getAttribute("key"));
		assertTrue(m1.hasAttribute("key"));
		assertEquals("lala2", m1.getAttribute("key2"));
		assertTrue(m1.hasAttribute("key2"));
		assertEquals("42", m1.getAttribute("life"));
		assertTrue(m1.hasAttribute("life"));
		assertEquals("node", m1.getName());
	}
	
	@Test
	public void testMarkupStartMultiAttributesSimplequoteUnspaced() throws InvalidMarkupException {
		m1 = new Markup("<node key='value' key2='lala2' life='42'>");
		assertEquals(Markup.START, m1.getType());
		assertEquals("", m1.getText());
		assertEquals("value", m1.getAttribute("key"));
		assertTrue(m1.hasAttribute("key"));
		assertEquals("lala2", m1.getAttribute("key2"));
		assertTrue(m1.hasAttribute("key2"));
		assertEquals("42", m1.getAttribute("life"));
		assertTrue(m1.hasAttribute("life"));
		assertEquals("node", m1.getName());
	}
	
	@Test
	public void testMarkupStartMultiAttributesEmptyUnspaced() throws InvalidMarkupException {
		m1 = new Markup("<node key=\"value\" key2='' life=\"\">");
		assertEquals(Markup.START, m1.getType());
		assertEquals("", m1.getText());
		assertEquals("value", m1.getAttribute("key"));
		assertTrue(m1.hasAttribute("key"));
		assertEquals("", m1.getAttribute("key2"));
		assertTrue(m1.hasAttribute("key2"));
		assertEquals("", m1.getAttribute("life"));
		assertTrue(m1.hasAttribute("life"));
		assertEquals("node", m1.getName());
	}
	
	@Test
	public void testMarkupEmptyMultiAttributesSpacedText() throws InvalidMarkupException {
		m1 = new Markup("<node key=\"value\" key2=\"lala2\" life=\"42\" >TEEEXT 123</node>");
		assertEquals(Markup.COMPLETE, m1.getType());
		assertEquals("TEEEXT 123", m1.getText());
		assertEquals("value", m1.getAttribute("key"));
		assertTrue(m1.hasAttribute("key"));
		assertEquals("lala2", m1.getAttribute("key2"));
		assertTrue(m1.hasAttribute("key2"));
		assertEquals("42", m1.getAttribute("life"));
		assertTrue(m1.hasAttribute("life"));
		assertEquals("node", m1.getName());
	}
	
	@Test(expected=InvalidMarkupException.class)
	public void testMarkupEmptyMultiAttributesSpacedTextInvalidNames() throws InvalidMarkupException {
		m1 = new Markup("<node key=\"value\" key2=\"lala2\" life=\"42\" >TEEEXT 123</meta>");
	}
	
	@Test
	public void testMarkupDeclarationSimpleUnspaced() throws InvalidMarkupException {
		m1 = new Markup("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		assertEquals(Markup.DECLARATION, m1.getType());
		assertEquals("", m1.getText());
		assertEquals("1.0", m1.getAttribute("version"));
		assertTrue(m1.hasAttribute("version"));
		assertEquals("UTF-8", m1.getAttribute("encoding"));
		assertTrue(m1.hasAttribute("encoding"));
		assertEquals("xml", m1.getName());
	}
	
	@Test(expected=InvalidMarkupException.class)
	public void testMarkupDeclarationSimpleUnspacedInvalid() throws InvalidMarkupException {
		m1 = new Markup("<?xml version=\"1.0\" encoding=\"UTF-8\"/>");
	}
	
	@Test
	public void testMarkupDeclarationSimpleSpaced() throws InvalidMarkupException {
		m1 = new Markup("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		assertEquals(Markup.DECLARATION, m1.getType());
		assertEquals("", m1.getText());
		assertEquals("1.0", m1.getAttribute("version"));
		assertTrue(m1.hasAttribute("version"));
		assertEquals("UTF-8", m1.getAttribute("encoding"));
		assertTrue(m1.hasAttribute("encoding"));
		assertEquals("xml", m1.getName());
	}
}