////////////////////////////////////////////////////////////////////////
//
// VoITrustAttrType.java
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

public class VoITrustAttrType extends com.altova.xml.TypeBase
{
	public static com.altova.xml.meta.ComplexType getStaticInfo() { return new com.altova.xml.meta.ComplexType(com.MetaData.MetaData_TypeInfo.binder.getTypes()[com.MetaData.MetaData_TypeInfo._altova_ti_altova_VoITrustAttrType]); }
	
	public VoITrustAttrType(org.w3c.dom.Node init)
	{
		super(init);
		instantiateMembers();
	}

	private void instantiateMembers()
	{

		sourceReputation= new MemberElement_sourceReputation (this, com.MetaData.MetaData_TypeInfo.binder.getMembers()[com.MetaData.MetaData_TypeInfo._altova_mi_altova_VoITrustAttrType._sourceReputation]);
		channelReputation= new MemberElement_channelReputation (this, com.MetaData.MetaData_TypeInfo.binder.getMembers()[com.MetaData.MetaData_TypeInfo._altova_mi_altova_VoITrustAttrType._channelReputation]);
		sourceObjectivity= new MemberElement_sourceObjectivity (this, com.MetaData.MetaData_TypeInfo.binder.getMembers()[com.MetaData.MetaData_TypeInfo._altova_mi_altova_VoITrustAttrType._sourceObjectivity]);
	}
	// Attributes


	// Elements
	
	public MemberElement_sourceReputation sourceReputation;

		public static class MemberElement_sourceReputation
		{
			public static class MemberElement_sourceReputation_Iterator implements java.util.Iterator
			{
				private org.w3c.dom.Node nextNode;
				private MemberElement_sourceReputation member;
				public MemberElement_sourceReputation_Iterator(MemberElement_sourceReputation member) {this.member=member; nextNode=member.owner.getElementFirst(member.info);}
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
					QoIMetric nx = new QoIMetric(nextNode);
					nextNode = nextNode.getNextSibling();
					return nx;
				}
				
				public void remove () {}
			}
			protected com.altova.xml.TypeBase owner;
			protected com.altova.typeinfo.MemberInfo info;
			public MemberElement_sourceReputation (com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) { this.owner = owner; this.info = info;}
			public QoIMetric at(int index) {return new QoIMetric(owner.getElementAt(info, index));}
			public QoIMetric first() {return new QoIMetric(owner.getElementFirst(info));}
			public QoIMetric last(){return new QoIMetric(owner.getElementLast(info));}
			public QoIMetric append(){return new QoIMetric(owner.createElement(info));}
			public boolean exists() {return count() > 0;}
			public int count() {return owner.countElement(info);}
			public void remove() {owner.removeElement(info);}
			public void removeAt(int index) {owner.removeElementAt(info, index);}
			public java.util.Iterator iterator() {return new MemberElement_sourceReputation_Iterator(this);}
			public com.altova.xml.meta.Element getInfo() { return new com.altova.xml.meta.Element(info); }
		}
	
	public MemberElement_channelReputation channelReputation;

		public static class MemberElement_channelReputation
		{
			public static class MemberElement_channelReputation_Iterator implements java.util.Iterator
			{
				private org.w3c.dom.Node nextNode;
				private MemberElement_channelReputation member;
				public MemberElement_channelReputation_Iterator(MemberElement_channelReputation member) {this.member=member; nextNode=member.owner.getElementFirst(member.info);}
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
					QoIMetric nx = new QoIMetric(nextNode);
					nextNode = nextNode.getNextSibling();
					return nx;
				}
				
				public void remove () {}
			}
			protected com.altova.xml.TypeBase owner;
			protected com.altova.typeinfo.MemberInfo info;
			public MemberElement_channelReputation (com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) { this.owner = owner; this.info = info;}
			public QoIMetric at(int index) {return new QoIMetric(owner.getElementAt(info, index));}
			public QoIMetric first() {return new QoIMetric(owner.getElementFirst(info));}
			public QoIMetric last(){return new QoIMetric(owner.getElementLast(info));}
			public QoIMetric append(){return new QoIMetric(owner.createElement(info));}
			public boolean exists() {return count() > 0;}
			public int count() {return owner.countElement(info);}
			public void remove() {owner.removeElement(info);}
			public void removeAt(int index) {owner.removeElementAt(info, index);}
			public java.util.Iterator iterator() {return new MemberElement_channelReputation_Iterator(this);}
			public com.altova.xml.meta.Element getInfo() { return new com.altova.xml.meta.Element(info); }
		}
	
	public MemberElement_sourceObjectivity sourceObjectivity;

		public static class MemberElement_sourceObjectivity
		{
			public static class MemberElement_sourceObjectivity_Iterator implements java.util.Iterator
			{
				private org.w3c.dom.Node nextNode;
				private MemberElement_sourceObjectivity member;
				public MemberElement_sourceObjectivity_Iterator(MemberElement_sourceObjectivity member) {this.member=member; nextNode=member.owner.getElementFirst(member.info);}
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
					QoIMetric nx = new QoIMetric(nextNode);
					nextNode = nextNode.getNextSibling();
					return nx;
				}
				
				public void remove () {}
			}
			protected com.altova.xml.TypeBase owner;
			protected com.altova.typeinfo.MemberInfo info;
			public MemberElement_sourceObjectivity (com.altova.xml.TypeBase owner, com.altova.typeinfo.MemberInfo info) { this.owner = owner; this.info = info;}
			public QoIMetric at(int index) {return new QoIMetric(owner.getElementAt(info, index));}
			public QoIMetric first() {return new QoIMetric(owner.getElementFirst(info));}
			public QoIMetric last(){return new QoIMetric(owner.getElementLast(info));}
			public QoIMetric append(){return new QoIMetric(owner.createElement(info));}
			public boolean exists() {return count() > 0;}
			public int count() {return owner.countElement(info);}
			public void remove() {owner.removeElement(info);}
			public void removeAt(int index) {owner.removeElementAt(info, index);}
			public java.util.Iterator iterator() {return new MemberElement_sourceObjectivity_Iterator(this);}
			public com.altova.xml.meta.Element getInfo() { return new com.altova.xml.meta.Element(info); }
		}
}
