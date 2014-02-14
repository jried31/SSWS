////////////////////////////////////////////////////////////////////////
//
// MetaData.java
//
// This file was generated by XMLSpy 2014 Enterprise Edition.
//
// YOU SHOULD NOT MODIFY THIS FILE, BECAUSE IT WILL BE
// OVERWRITTEN WHEN YOU RE-RUN CODE GENERATION.
//
// Refer to the XMLSpy Documentation for further details.
// http://www.altova.com/xmlspy
//
////////////////////////////////////////////////////////////////////////

package com.MetaData;

public class MetaData extends com.altova.xml.TypeBase
{
	public static com.altova.xml.meta.ComplexType getStaticInfo() { return new com.altova.xml.meta.ComplexType(com.MetaData.MetaData_TypeInfo.binder.getTypes()[com.MetaData.MetaData_TypeInfo._altova_ti_altova_MetaData]); }

	public static MetaData loadFromFile(String filename) throws Exception
	{
		return new MetaData(com.altova.xml.XmlTreeOperations.loadDocument(filename));			
	}

	public static MetaData loadFromString(String xmlstring) throws Exception
	{
		return new MetaData(com.altova.xml.XmlTreeOperations.loadXml(xmlstring));			
	}

	public static MetaData loadFromBinary(byte[] binary) throws Exception
	{
		return new MetaData(com.altova.xml.XmlTreeOperations.loadXmlBinary(binary));
	}

	public void saveToFile(String filename, boolean prettyPrint) throws Exception
	{
		saveToFile(filename, prettyPrint, "UTF-8", false, false);
	}
	
	public void saveToFile(String filename, boolean prettyPrint, String encoding) throws Exception
	{
		saveToFile( filename, prettyPrint, encoding, encoding.compareToIgnoreCase("UTF-16BE") == 0, encoding.compareToIgnoreCase("UTF-16") == 0 );
	}

	public void saveToFile(String filename, boolean prettyPrint, String encoding, boolean bBigEndian, boolean bBOM) throws Exception
	{
		org.w3c.dom.Document doc = (org.w3c.dom.Document) getNode();
		com.altova.xml.XmlTreeOperations.saveDocument(doc, filename, encoding, bBigEndian, bBOM, prettyPrint);
	}

	public String saveToString(boolean prettyPrint) throws Exception
	{
		org.w3c.dom.Document doc = (org.w3c.dom.Document) getNode();
		return com.altova.xml.XmlTreeOperations.saveXml(doc, prettyPrint);
	}

	public byte[] saveToBinary(boolean prettyPrint) throws Exception
	{
		return saveToBinary(prettyPrint, "UTF-8", false, false);
	}
	
	public byte[] saveToBinary(boolean prettyPrint, String encoding) throws Exception
	{
		return saveToBinary( prettyPrint, encoding, encoding.compareToIgnoreCase("UTF-16BE") == 0, encoding.compareToIgnoreCase("UTF-16") == 0 );
	}

	public byte[] saveToBinary(boolean prettyPrint, String encoding, boolean bBigEndian, boolean bBOM) throws Exception
	{
		org.w3c.dom.Document doc = (org.w3c.dom.Document) getNode();
		return com.altova.xml.XmlTreeOperations.saveXmlBinary(doc, encoding, bBigEndian, bBOM, prettyPrint);
	}

	public static MetaData createDocument() throws Exception
	{
		org.w3c.dom.Document doc = com.altova.xml.XmlTreeOperations.createDocument();
		return new MetaData(doc);
	}

	public void setSchemaLocation(String schemaLocation) throws Exception
	{
		org.w3c.dom.Document doc = (org.w3c.dom.Document) node;
		if (doc.getDocumentElement() == null)
			throw new Exception("SetSchemaLocation requires a root element.");
		String namespaceuri = doc.getDocumentElement().getNamespaceURI();
		if (namespaceuri == null || namespaceuri.length() == 0)
			doc.getDocumentElement().setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "noNamespaceSchemaLocation", schemaLocation);
		else
			doc.getDocumentElement().setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "schemaLocation", namespaceuri + " " + schemaLocation);
	}
	
	public MetaData(org.w3c.dom.Node init)
	{
		super(init);
		instantiateMembers();
	}

	private void instantiateMembers()
	{

		BaseData= new MemberElement_BaseData (this, com.MetaData.MetaData_TypeInfo.binder.getMembers()[com.MetaData.MetaData_TypeInfo._altova_mi_altova_MetaData._BaseData]);
		BaseQoI= new MemberElement_BaseQoI (this, com.MetaData.MetaData_TypeInfo.binder.getMembers()[com.MetaData.MetaData_TypeInfo._altova_mi_altova_MetaData._BaseQoI]);
		BaseVoI= new MemberElement_BaseVoI (this, com.MetaData.MetaData_TypeInfo.binder.getMembers()[com.MetaData.MetaData_TypeInfo._altova_mi_altova_MetaData._BaseVoI]);
		Contact= new MemberElement_Contact (this, com.MetaData.MetaData_TypeInfo.binder.getMembers()[com.MetaData.MetaData_TypeInfo._altova_mi_altova_MetaData._Contact]);
		Context= new MemberElement_Context (this, com.MetaData.MetaData_TypeInfo.binder.getMembers()[com.MetaData.MetaData_TypeInfo._altova_mi_altova_MetaData._Context]);
		DataSource= new MemberElement_DataSource (this, com.MetaData.MetaData_TypeInfo.binder.getMembers()[com.MetaData.MetaData_TypeInfo._altova_mi_altova_MetaData._DataSource]);
		Information= new MemberElement_Information (this, com.MetaData.MetaData_TypeInfo.binder.getMembers()[com.MetaData.MetaData_TypeInfo._altova_mi_altova_MetaData._Information]);
		QoIBaseAttr= new MemberElement_QoIBaseAttr (this, com.MetaData.MetaData_TypeInfo.binder.getMembers()[com.MetaData.MetaData_TypeInfo._altova_mi_altova_MetaData._QoIBaseAttr]);
		VoIBaseAttr= new MemberElement_VoIBaseAttr (this, com.MetaData.MetaData_TypeInfo.binder.getMembers()[com.MetaData.MetaData_TypeInfo._altova_mi_altova_MetaData._VoIBaseAttr]);
	}
	// Attributes


	// Elements
	
	public MemberElement_BaseData BaseData;

		public static class MemberElement_BaseData
		{
			public static class MemberElement_BaseData_Iterator implements java.util.Iterator
			{
				private org.w3c.dom.Node nextNode;
				private MemberElement_BaseData member;
				public MemberElement_BaseData_Iterator(MemberElement_BaseData member) {this.member=member; nextNode=member.owner.getElementFirst(member.info);}
				public boolean hasNext() 
				{
					while (nextNode != null)
					{
						if (com.altova.xml.TypeBase.memberEqualsNode(member.info, nextNode))
							return true;
						nextNode = nextNode.getNextSibling();
					}
					return false;
				}
				
				public Object next()
				{
					BaseDataType nx = new BaseDataType(nextNode);
					nextNode = nextNode.getNextSibling();
					return nx;
				}
				
				public void remove () {}
			}
			protected com.altova.xml.TypeBase owner;
			protected com.altova.typeinfo.MemberInfo info;
			public MemberElement_BaseData (com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) { this.owner = owner; this.info = info;}
			public BaseDataType at(int index) {return new BaseDataType(owner.getElementAt(info, index));}
			public BaseDataType first() {return new BaseDataType(owner.getElementFirst(info));}
			public BaseDataType last(){return new BaseDataType(owner.getElementLast(info));}
			public BaseDataType append(){return new BaseDataType(owner.createElement(info));}
			public boolean exists() {return count() > 0;}
			public int count() {return owner.countElement(info);}
			public void remove() {owner.removeElement(info);}
			public void removeAt(int index) {owner.removeElementAt(info, index);}
			public java.util.Iterator iterator() {return new MemberElement_BaseData_Iterator(this);}
			public com.altova.xml.meta.Element getInfo() { return new com.altova.xml.meta.Element(info); }
		}
	
	public MemberElement_BaseQoI BaseQoI;

		public static class MemberElement_BaseQoI
		{
			public static class MemberElement_BaseQoI_Iterator implements java.util.Iterator
			{
				private org.w3c.dom.Node nextNode;
				private MemberElement_BaseQoI member;
				public MemberElement_BaseQoI_Iterator(MemberElement_BaseQoI member) {this.member=member; nextNode=member.owner.getElementFirst(member.info);}
				public boolean hasNext() 
				{
					while (nextNode != null)
					{
						if (com.altova.xml.TypeBase.memberEqualsNode(member.info, nextNode))
							return true;
						nextNode = nextNode.getNextSibling();
					}
					return false;
				}
				
				public Object next()
				{
					BaseQoIType nx = new BaseQoIType(nextNode);
					nextNode = nextNode.getNextSibling();
					return nx;
				}
				
				public void remove () {}
			}
			protected com.altova.xml.TypeBase owner;
			protected com.altova.typeinfo.MemberInfo info;
			public MemberElement_BaseQoI (com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) { this.owner = owner; this.info = info;}
			public BaseQoIType at(int index) {return new BaseQoIType(owner.getElementAt(info, index));}
			public BaseQoIType first() {return new BaseQoIType(owner.getElementFirst(info));}
			public BaseQoIType last(){return new BaseQoIType(owner.getElementLast(info));}
			public BaseQoIType append(){return new BaseQoIType(owner.createElement(info));}
			public boolean exists() {return count() > 0;}
			public int count() {return owner.countElement(info);}
			public void remove() {owner.removeElement(info);}
			public void removeAt(int index) {owner.removeElementAt(info, index);}
			public java.util.Iterator iterator() {return new MemberElement_BaseQoI_Iterator(this);}
			public com.altova.xml.meta.Element getInfo() { return new com.altova.xml.meta.Element(info); }
		}
	
	public MemberElement_BaseVoI BaseVoI;

		public static class MemberElement_BaseVoI
		{
			public static class MemberElement_BaseVoI_Iterator implements java.util.Iterator
			{
				private org.w3c.dom.Node nextNode;
				private MemberElement_BaseVoI member;
				public MemberElement_BaseVoI_Iterator(MemberElement_BaseVoI member) {this.member=member; nextNode=member.owner.getElementFirst(member.info);}
				public boolean hasNext() 
				{
					while (nextNode != null)
					{
						if (com.altova.xml.TypeBase.memberEqualsNode(member.info, nextNode))
							return true;
						nextNode = nextNode.getNextSibling();
					}
					return false;
				}
				
				public Object next()
				{
					BaseVoIType nx = new BaseVoIType(nextNode);
					nextNode = nextNode.getNextSibling();
					return nx;
				}
				
				public void remove () {}
			}
			protected com.altova.xml.TypeBase owner;
			protected com.altova.typeinfo.MemberInfo info;
			public MemberElement_BaseVoI (com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) { this.owner = owner; this.info = info;}
			public BaseVoIType at(int index) {return new BaseVoIType(owner.getElementAt(info, index));}
			public BaseVoIType first() {return new BaseVoIType(owner.getElementFirst(info));}
			public BaseVoIType last(){return new BaseVoIType(owner.getElementLast(info));}
			public BaseVoIType append(){return new BaseVoIType(owner.createElement(info));}
			public boolean exists() {return count() > 0;}
			public int count() {return owner.countElement(info);}
			public void remove() {owner.removeElement(info);}
			public void removeAt(int index) {owner.removeElementAt(info, index);}
			public java.util.Iterator iterator() {return new MemberElement_BaseVoI_Iterator(this);}
			public com.altova.xml.meta.Element getInfo() { return new com.altova.xml.meta.Element(info); }
		}
	
	public MemberElement_Contact Contact;

		public static class MemberElement_Contact
		{
			public static class MemberElement_Contact_Iterator implements java.util.Iterator
			{
				private org.w3c.dom.Node nextNode;
				private MemberElement_Contact member;
				public MemberElement_Contact_Iterator(MemberElement_Contact member) {this.member=member; nextNode=member.owner.getElementFirst(member.info);}
				public boolean hasNext() 
				{
					while (nextNode != null)
					{
						if (com.altova.xml.TypeBase.memberEqualsNode(member.info, nextNode))
							return true;
						nextNode = nextNode.getNextSibling();
					}
					return false;
				}
				
				public Object next()
				{
					ContactType nx = new ContactType(nextNode);
					nextNode = nextNode.getNextSibling();
					return nx;
				}
				
				public void remove () {}
			}
			protected com.altova.xml.TypeBase owner;
			protected com.altova.typeinfo.MemberInfo info;
			public MemberElement_Contact (com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) { this.owner = owner; this.info = info;}
			public ContactType at(int index) {return new ContactType(owner.getElementAt(info, index));}
			public ContactType first() {return new ContactType(owner.getElementFirst(info));}
			public ContactType last(){return new ContactType(owner.getElementLast(info));}
			public ContactType append(){return new ContactType(owner.createElement(info));}
			public boolean exists() {return count() > 0;}
			public int count() {return owner.countElement(info);}
			public void remove() {owner.removeElement(info);}
			public void removeAt(int index) {owner.removeElementAt(info, index);}
			public java.util.Iterator iterator() {return new MemberElement_Contact_Iterator(this);}
			public com.altova.xml.meta.Element getInfo() { return new com.altova.xml.meta.Element(info); }
		}
	
	public MemberElement_Context Context;

		public static class MemberElement_Context
		{
			public static class MemberElement_Context_Iterator implements java.util.Iterator
			{
				private org.w3c.dom.Node nextNode;
				private MemberElement_Context member;
				public MemberElement_Context_Iterator(MemberElement_Context member) {this.member=member; nextNode=member.owner.getElementFirst(member.info);}
				public boolean hasNext() 
				{
					while (nextNode != null)
					{
						if (com.altova.xml.TypeBase.memberEqualsNode(member.info, nextNode))
							return true;
						nextNode = nextNode.getNextSibling();
					}
					return false;
				}
				
				public Object next()
				{
					ContextType nx = new ContextType(nextNode);
					nextNode = nextNode.getNextSibling();
					return nx;
				}
				
				public void remove () {}
			}
			protected com.altova.xml.TypeBase owner;
			protected com.altova.typeinfo.MemberInfo info;
			public MemberElement_Context (com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) { this.owner = owner; this.info = info;}
			public ContextType at(int index) {return new ContextType(owner.getElementAt(info, index));}
			public ContextType first() {return new ContextType(owner.getElementFirst(info));}
			public ContextType last(){return new ContextType(owner.getElementLast(info));}
			public ContextType append(){return new ContextType(owner.createElement(info));}
			public boolean exists() {return count() > 0;}
			public int count() {return owner.countElement(info);}
			public void remove() {owner.removeElement(info);}
			public void removeAt(int index) {owner.removeElementAt(info, index);}
			public java.util.Iterator iterator() {return new MemberElement_Context_Iterator(this);}
			public com.altova.xml.meta.Element getInfo() { return new com.altova.xml.meta.Element(info); }
		}
	
	public MemberElement_DataSource DataSource;

		public static class MemberElement_DataSource
		{
			public static class MemberElement_DataSource_Iterator implements java.util.Iterator
			{
				private org.w3c.dom.Node nextNode;
				private MemberElement_DataSource member;
				public MemberElement_DataSource_Iterator(MemberElement_DataSource member) {this.member=member; nextNode=member.owner.getElementFirst(member.info);}
				public boolean hasNext() 
				{
					while (nextNode != null)
					{
						if (com.altova.xml.TypeBase.memberEqualsNode(member.info, nextNode))
							return true;
						nextNode = nextNode.getNextSibling();
					}
					return false;
				}
				
				public Object next()
				{
					DataSourceType nx = new DataSourceType(nextNode);
					nextNode = nextNode.getNextSibling();
					return nx;
				}
				
				public void remove () {}
			}
			protected com.altova.xml.TypeBase owner;
			protected com.altova.typeinfo.MemberInfo info;
			public MemberElement_DataSource (com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) { this.owner = owner; this.info = info;}
			public DataSourceType at(int index) {return new DataSourceType(owner.getElementAt(info, index));}
			public DataSourceType first() {return new DataSourceType(owner.getElementFirst(info));}
			public DataSourceType last(){return new DataSourceType(owner.getElementLast(info));}
			public DataSourceType append(){return new DataSourceType(owner.createElement(info));}
			public boolean exists() {return count() > 0;}
			public int count() {return owner.countElement(info);}
			public void remove() {owner.removeElement(info);}
			public void removeAt(int index) {owner.removeElementAt(info, index);}
			public java.util.Iterator iterator() {return new MemberElement_DataSource_Iterator(this);}
			public com.altova.xml.meta.Element getInfo() { return new com.altova.xml.meta.Element(info); }
		}
	
	public MemberElement_Information Information;

		public static class MemberElement_Information
		{
			public static class MemberElement_Information_Iterator implements java.util.Iterator
			{
				private org.w3c.dom.Node nextNode;
				private MemberElement_Information member;
				public MemberElement_Information_Iterator(MemberElement_Information member) {this.member=member; nextNode=member.owner.getElementFirst(member.info);}
				public boolean hasNext() 
				{
					while (nextNode != null)
					{
						if (com.altova.xml.TypeBase.memberEqualsNode(member.info, nextNode))
							return true;
						nextNode = nextNode.getNextSibling();
					}
					return false;
				}
				
				public Object next()
				{
					InformationType nx = new InformationType(nextNode);
					nextNode = nextNode.getNextSibling();
					return nx;
				}
				
				public void remove () {}
			}
			protected com.altova.xml.TypeBase owner;
			protected com.altova.typeinfo.MemberInfo info;
			public MemberElement_Information (com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) { this.owner = owner; this.info = info;}
			public InformationType at(int index) {return new InformationType(owner.getElementAt(info, index));}
			public InformationType first() {return new InformationType(owner.getElementFirst(info));}
			public InformationType last(){return new InformationType(owner.getElementLast(info));}
			public InformationType append(){return new InformationType(owner.createElement(info));}
			public boolean exists() {return count() > 0;}
			public int count() {return owner.countElement(info);}
			public void remove() {owner.removeElement(info);}
			public void removeAt(int index) {owner.removeElementAt(info, index);}
			public java.util.Iterator iterator() {return new MemberElement_Information_Iterator(this);}
			public com.altova.xml.meta.Element getInfo() { return new com.altova.xml.meta.Element(info); }
		}
	
	public MemberElement_QoIBaseAttr QoIBaseAttr;

		public static class MemberElement_QoIBaseAttr
		{
			public static class MemberElement_QoIBaseAttr_Iterator implements java.util.Iterator
			{
				private org.w3c.dom.Node nextNode;
				private MemberElement_QoIBaseAttr member;
				public MemberElement_QoIBaseAttr_Iterator(MemberElement_QoIBaseAttr member) {this.member=member; nextNode=member.owner.getElementFirst(member.info);}
				public boolean hasNext() 
				{
					while (nextNode != null)
					{
						if (com.altova.xml.TypeBase.memberEqualsNode(member.info, nextNode))
							return true;
						nextNode = nextNode.getNextSibling();
					}
					return false;
				}
				
				public Object next()
				{
					QoIBaseAttrType nx = new QoIBaseAttrType(nextNode);
					nextNode = nextNode.getNextSibling();
					return nx;
				}
				
				public void remove () {}
			}
			protected com.altova.xml.TypeBase owner;
			protected com.altova.typeinfo.MemberInfo info;
			public MemberElement_QoIBaseAttr (com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) { this.owner = owner; this.info = info;}
			public QoIBaseAttrType at(int index) {return new QoIBaseAttrType(owner.getElementAt(info, index));}
			public QoIBaseAttrType first() {return new QoIBaseAttrType(owner.getElementFirst(info));}
			public QoIBaseAttrType last(){return new QoIBaseAttrType(owner.getElementLast(info));}
			public QoIBaseAttrType append(){return new QoIBaseAttrType(owner.createElement(info));}
			public boolean exists() {return count() > 0;}
			public int count() {return owner.countElement(info);}
			public void remove() {owner.removeElement(info);}
			public void removeAt(int index) {owner.removeElementAt(info, index);}
			public java.util.Iterator iterator() {return new MemberElement_QoIBaseAttr_Iterator(this);}
			public com.altova.xml.meta.Element getInfo() { return new com.altova.xml.meta.Element(info); }
		}
	
	public MemberElement_VoIBaseAttr VoIBaseAttr;

		public static class MemberElement_VoIBaseAttr
		{
			public static class MemberElement_VoIBaseAttr_Iterator implements java.util.Iterator
			{
				private org.w3c.dom.Node nextNode;
				private MemberElement_VoIBaseAttr member;
				public MemberElement_VoIBaseAttr_Iterator(MemberElement_VoIBaseAttr member) {this.member=member; nextNode=member.owner.getElementFirst(member.info);}
				public boolean hasNext() 
				{
					while (nextNode != null)
					{
						if (com.altova.xml.TypeBase.memberEqualsNode(member.info, nextNode))
							return true;
						nextNode = nextNode.getNextSibling();
					}
					return false;
				}
				
				public Object next()
				{
					VoIBaseAttrType nx = new VoIBaseAttrType(nextNode);
					nextNode = nextNode.getNextSibling();
					return nx;
				}
				
				public void remove () {}
			}
			protected com.altova.xml.TypeBase owner;
			protected com.altova.typeinfo.MemberInfo info;
			public MemberElement_VoIBaseAttr (com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) { this.owner = owner; this.info = info;}
			public VoIBaseAttrType at(int index) {return new VoIBaseAttrType(owner.getElementAt(info, index));}
			public VoIBaseAttrType first() {return new VoIBaseAttrType(owner.getElementFirst(info));}
			public VoIBaseAttrType last(){return new VoIBaseAttrType(owner.getElementLast(info));}
			public VoIBaseAttrType append(){return new VoIBaseAttrType(owner.createElement(info));}
			public boolean exists() {return count() > 0;}
			public int count() {return owner.countElement(info);}
			public void remove() {owner.removeElement(info);}
			public void removeAt(int index) {owner.removeElementAt(info, index);}
			public java.util.Iterator iterator() {return new MemberElement_VoIBaseAttr_Iterator(this);}
			public com.altova.xml.meta.Element getInfo() { return new com.altova.xml.meta.Element(info); }
		}

		public void setXsiType() {com.altova.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "", "MetaData");}
}