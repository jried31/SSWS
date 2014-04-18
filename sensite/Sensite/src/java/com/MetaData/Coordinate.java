////////////////////////////////////////////////////////////////////////
//
// Coordinate.java
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

public class Coordinate extends com.altova.xml.TypeBase
{
	public static com.altova.xml.meta.ComplexType getStaticInfo() { return new com.altova.xml.meta.ComplexType(com.MetaData.MetaData_TypeInfo.binder.getTypes()[com.MetaData.MetaData_TypeInfo._altova_ti_altova_Coordinate]); }
	
	public Coordinate(org.w3c.dom.Node init)
	{
		super(init);
		instantiateMembers();
	}

	private void instantiateMembers()
	{

		latitude= new MemberElement_latitude (this, com.MetaData.MetaData_TypeInfo.binder.getMembers()[com.MetaData.MetaData_TypeInfo._altova_mi_altova_Coordinate._latitude]);
		longitude= new MemberElement_longitude (this, com.MetaData.MetaData_TypeInfo.binder.getMembers()[com.MetaData.MetaData_TypeInfo._altova_mi_altova_Coordinate._longitude]);
		depth= new MemberElement_depth (this, com.MetaData.MetaData_TypeInfo.binder.getMembers()[com.MetaData.MetaData_TypeInfo._altova_mi_altova_Coordinate._depth]);
		orientation= new MemberElement_orientation (this, com.MetaData.MetaData_TypeInfo.binder.getMembers()[com.MetaData.MetaData_TypeInfo._altova_mi_altova_Coordinate._orientation]);
	}
	// Attributes


	// Elements
	
	public MemberElement_latitude latitude;

		public static class MemberElement_latitude
		{
			public static class MemberElement_latitude_Iterator implements java.util.Iterator
			{
				private org.w3c.dom.Node nextNode;
				private MemberElement_latitude member;
				public MemberElement_latitude_Iterator(MemberElement_latitude member) {this.member=member; nextNode=member.owner.getElementFirst(member.info);}
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
					com.MetaData.xs.anyType nx = new com.MetaData.xs.anyType(nextNode);
					nextNode = nextNode.getNextSibling();
					return nx;
				}
				
				public void remove () {}
			}
			protected com.altova.xml.TypeBase owner;
			protected com.altova.typeinfo.MemberInfo info;
			public MemberElement_latitude (com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) { this.owner = owner; this.info = info;}
			public com.MetaData.xs.anyType at(int index) {return new com.MetaData.xs.anyType(owner.getElementAt(info, index));}
			public com.MetaData.xs.anyType first() {return new com.MetaData.xs.anyType(owner.getElementFirst(info));}
			public com.MetaData.xs.anyType last(){return new com.MetaData.xs.anyType(owner.getElementLast(info));}
			public com.MetaData.xs.anyType append(){return new com.MetaData.xs.anyType(owner.createElement(info));}
			public boolean exists() {return count() > 0;}
			public int count() {return owner.countElement(info);}
			public void remove() {owner.removeElement(info);}
			public void removeAt(int index) {owner.removeElementAt(info, index);}
			public java.util.Iterator iterator() {return new MemberElement_latitude_Iterator(this);}
			public com.altova.xml.meta.Element getInfo() { return new com.altova.xml.meta.Element(info); }
		}
	
	public MemberElement_longitude longitude;

		public static class MemberElement_longitude
		{
			public static class MemberElement_longitude_Iterator implements java.util.Iterator
			{
				private org.w3c.dom.Node nextNode;
				private MemberElement_longitude member;
				public MemberElement_longitude_Iterator(MemberElement_longitude member) {this.member=member; nextNode=member.owner.getElementFirst(member.info);}
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
					com.MetaData.xs.anyType nx = new com.MetaData.xs.anyType(nextNode);
					nextNode = nextNode.getNextSibling();
					return nx;
				}
				
				public void remove () {}
			}
			protected com.altova.xml.TypeBase owner;
			protected com.altova.typeinfo.MemberInfo info;
			public MemberElement_longitude (com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) { this.owner = owner; this.info = info;}
			public com.MetaData.xs.anyType at(int index) {return new com.MetaData.xs.anyType(owner.getElementAt(info, index));}
			public com.MetaData.xs.anyType first() {return new com.MetaData.xs.anyType(owner.getElementFirst(info));}
			public com.MetaData.xs.anyType last(){return new com.MetaData.xs.anyType(owner.getElementLast(info));}
			public com.MetaData.xs.anyType append(){return new com.MetaData.xs.anyType(owner.createElement(info));}
			public boolean exists() {return count() > 0;}
			public int count() {return owner.countElement(info);}
			public void remove() {owner.removeElement(info);}
			public void removeAt(int index) {owner.removeElementAt(info, index);}
			public java.util.Iterator iterator() {return new MemberElement_longitude_Iterator(this);}
			public com.altova.xml.meta.Element getInfo() { return new com.altova.xml.meta.Element(info); }
		}
	
	public MemberElement_depth depth;

		public static class MemberElement_depth
		{
			public static class MemberElement_depth_Iterator implements java.util.Iterator
			{
				private org.w3c.dom.Node nextNode;
				private MemberElement_depth member;
				public MemberElement_depth_Iterator(MemberElement_depth member) {this.member=member; nextNode=member.owner.getElementFirst(member.info);}
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
					com.MetaData.xs.anyType nx = new com.MetaData.xs.anyType(nextNode);
					nextNode = nextNode.getNextSibling();
					return nx;
				}
				
				public void remove () {}
			}
			protected com.altova.xml.TypeBase owner;
			protected com.altova.typeinfo.MemberInfo info;
			public MemberElement_depth (com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) { this.owner = owner; this.info = info;}
			public com.MetaData.xs.anyType at(int index) {return new com.MetaData.xs.anyType(owner.getElementAt(info, index));}
			public com.MetaData.xs.anyType first() {return new com.MetaData.xs.anyType(owner.getElementFirst(info));}
			public com.MetaData.xs.anyType last(){return new com.MetaData.xs.anyType(owner.getElementLast(info));}
			public com.MetaData.xs.anyType append(){return new com.MetaData.xs.anyType(owner.createElement(info));}
			public boolean exists() {return count() > 0;}
			public int count() {return owner.countElement(info);}
			public void remove() {owner.removeElement(info);}
			public void removeAt(int index) {owner.removeElementAt(info, index);}
			public java.util.Iterator iterator() {return new MemberElement_depth_Iterator(this);}
			public com.altova.xml.meta.Element getInfo() { return new com.altova.xml.meta.Element(info); }
		}
	
	public MemberElement_orientation orientation;

		public static class MemberElement_orientation
		{
			public static class MemberElement_orientation_Iterator implements java.util.Iterator
			{
				private org.w3c.dom.Node nextNode;
				private MemberElement_orientation member;
				public MemberElement_orientation_Iterator(MemberElement_orientation member) {this.member=member; nextNode=member.owner.getElementFirst(member.info);}
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
					com.MetaData.xs.anyType nx = new com.MetaData.xs.anyType(nextNode);
					nextNode = nextNode.getNextSibling();
					return nx;
				}
				
				public void remove () {}
			}
			protected com.altova.xml.TypeBase owner;
			protected com.altova.typeinfo.MemberInfo info;
			public MemberElement_orientation (com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) { this.owner = owner; this.info = info;}
			public com.MetaData.xs.anyType at(int index) {return new com.MetaData.xs.anyType(owner.getElementAt(info, index));}
			public com.MetaData.xs.anyType first() {return new com.MetaData.xs.anyType(owner.getElementFirst(info));}
			public com.MetaData.xs.anyType last(){return new com.MetaData.xs.anyType(owner.getElementLast(info));}
			public com.MetaData.xs.anyType append(){return new com.MetaData.xs.anyType(owner.createElement(info));}
			public boolean exists() {return count() > 0;}
			public int count() {return owner.countElement(info);}
			public void remove() {owner.removeElement(info);}
			public void removeAt(int index) {owner.removeElementAt(info, index);}
			public java.util.Iterator iterator() {return new MemberElement_orientation_Iterator(this);}
			public com.altova.xml.meta.Element getInfo() { return new com.altova.xml.meta.Element(info); }
		}

		public void setXsiType() {com.altova.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "", "Coordinate");}
}
