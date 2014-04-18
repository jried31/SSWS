////////////////////////////////////////////////////////////////////////
//
// BaseMetric.java
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

public class BaseMetric extends com.altova.xml.TypeBase
{
	public static com.altova.xml.meta.ComplexType getStaticInfo() { return new com.altova.xml.meta.ComplexType(com.MetaData.MetaData_TypeInfo.binder.getTypes()[com.MetaData.MetaData_TypeInfo._altova_ti_altova_BaseMetric]); }
	
	public BaseMetric(org.w3c.dom.Node init)
	{
		super(init);
		instantiateMembers();
	}

	private void instantiateMembers()
	{

		EnumeratedMetric= new MemberElement_EnumeratedMetric (this, com.MetaData.MetaData_TypeInfo.binder.getMembers()[com.MetaData.MetaData_TypeInfo._altova_mi_altova_BaseMetric._EnumeratedMetric]);
		QuantitativeMetric= new MemberElement_QuantitativeMetric (this, com.MetaData.MetaData_TypeInfo.binder.getMembers()[com.MetaData.MetaData_TypeInfo._altova_mi_altova_BaseMetric._QuantitativeMetric]);
	}
	// Attributes


	// Elements
	
	public MemberElement_EnumeratedMetric EnumeratedMetric;

		public static class MemberElement_EnumeratedMetric
		{
			public static class MemberElement_EnumeratedMetric_Iterator implements java.util.Iterator
			{
				private org.w3c.dom.Node nextNode;
				private MemberElement_EnumeratedMetric member;
				public MemberElement_EnumeratedMetric_Iterator(MemberElement_EnumeratedMetric member) {this.member=member; nextNode=member.owner.getElementFirst(member.info);}
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
					EnumeratedMetricType nx = new EnumeratedMetricType(nextNode);
					nextNode = nextNode.getNextSibling();
					return nx;
				}
				
				public void remove () {}
			}
			protected com.altova.xml.TypeBase owner;
			protected com.altova.typeinfo.MemberInfo info;
			public MemberElement_EnumeratedMetric (com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) { this.owner = owner; this.info = info;}
			public EnumeratedMetricType at(int index) {return new EnumeratedMetricType(owner.getElementAt(info, index));}
			public EnumeratedMetricType first() {return new EnumeratedMetricType(owner.getElementFirst(info));}
			public EnumeratedMetricType last(){return new EnumeratedMetricType(owner.getElementLast(info));}
			public EnumeratedMetricType append(){return new EnumeratedMetricType(owner.createElement(info));}
			public boolean exists() {return count() > 0;}
			public int count() {return owner.countElement(info);}
			public void remove() {owner.removeElement(info);}
			public void removeAt(int index) {owner.removeElementAt(info, index);}
			public java.util.Iterator iterator() {return new MemberElement_EnumeratedMetric_Iterator(this);}
			public com.altova.xml.meta.Element getInfo() { return new com.altova.xml.meta.Element(info); }
		}
	
	public MemberElement_QuantitativeMetric QuantitativeMetric;

		public static class MemberElement_QuantitativeMetric
		{
			public static class MemberElement_QuantitativeMetric_Iterator implements java.util.Iterator
			{
				private org.w3c.dom.Node nextNode;
				private MemberElement_QuantitativeMetric member;
				public MemberElement_QuantitativeMetric_Iterator(MemberElement_QuantitativeMetric member) {this.member=member; nextNode=member.owner.getElementFirst(member.info);}
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
					com.MetaData.xs.doubleType nx = new com.MetaData.xs.doubleType(nextNode);
					nextNode = nextNode.getNextSibling();
					return nx;
				}
				
				public void remove () {}
			}
			protected com.altova.xml.TypeBase owner;
			protected com.altova.typeinfo.MemberInfo info;
			public MemberElement_QuantitativeMetric (com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) { this.owner = owner; this.info = info;}
			public com.MetaData.xs.doubleType at(int index) {return new com.MetaData.xs.doubleType(owner.getElementAt(info, index));}
			public com.MetaData.xs.doubleType first() {return new com.MetaData.xs.doubleType(owner.getElementFirst(info));}
			public com.MetaData.xs.doubleType last(){return new com.MetaData.xs.doubleType(owner.getElementLast(info));}
			public com.MetaData.xs.doubleType append(){return new com.MetaData.xs.doubleType(owner.createElement(info));}
			public boolean exists() {return count() > 0;}
			public int count() {return owner.countElement(info);}
			public void remove() {owner.removeElement(info);}
			public void removeAt(int index) {owner.removeElementAt(info, index);}
			public java.util.Iterator iterator() {return new MemberElement_QuantitativeMetric_Iterator(this);}
			public com.altova.xml.meta.Element getInfo() { return new com.altova.xml.meta.Element(info); }
		}

		public void setXsiType() {com.altova.xml.XmlTreeOperations.setAttribute(getNode(), "http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "", "BaseMetric");}
}
