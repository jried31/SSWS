////////////////////////////////////////////////////////////////////////
//
// BaseVoIType.java
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

public class BaseVoIType extends com.altova.xml.TypeBase
{
	public static com.altova.xml.meta.ComplexType getStaticInfo() { return new com.altova.xml.meta.ComplexType(com.MetaData.MetaData_TypeInfo.binder.getTypes()[com.MetaData.MetaData_TypeInfo._altova_ti_altova_BaseVoIType]); }
	
	public BaseVoIType(org.w3c.dom.Node init)
	{
		super(init);
		instantiateMembers();
	}

	private void instantiateMembers()
	{

		VoIBaseAttr= new MemberElement_VoIBaseAttr (this, com.MetaData.MetaData_TypeInfo.binder.getMembers()[com.MetaData.MetaData_TypeInfo._altova_mi_altova_BaseVoIType._VoIBaseAttr]);
		Context= new MemberElement_Context (this, com.MetaData.MetaData_TypeInfo.binder.getMembers()[com.MetaData.MetaData_TypeInfo._altova_mi_altova_BaseVoIType._Context]);
	}
	// Attributes


	// Elements
	
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
					ContextType2 nx = new ContextType2(nextNode);
					nextNode = nextNode.getNextSibling();
					return nx;
				}
				
				public void remove () {}
			}
			protected com.altova.xml.TypeBase owner;
			protected com.altova.typeinfo.MemberInfo info;
			public MemberElement_Context (com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) { this.owner = owner; this.info = info;}
			public ContextType2 at(int index) {return new ContextType2(owner.getElementAt(info, index));}
			public ContextType2 first() {return new ContextType2(owner.getElementFirst(info));}
			public ContextType2 last(){return new ContextType2(owner.getElementLast(info));}
			public ContextType2 append(){return new ContextType2(owner.createElement(info));}
			public boolean exists() {return count() > 0;}
			public int count() {return owner.countElement(info);}
			public void remove() {owner.removeElement(info);}
			public void removeAt(int index) {owner.removeElementAt(info, index);}
			public java.util.Iterator iterator() {return new MemberElement_Context_Iterator(this);}
			public com.altova.xml.meta.Element getInfo() { return new com.altova.xml.meta.Element(info); }
		}
}
