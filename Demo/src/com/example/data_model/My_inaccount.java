/**
 * 
 */
/**
 * @author ����
 *
 */
package com.example.data_model;
public class My_inaccount{
	private int _id;//�洢������
	private double money;//�洢������
	private String time;//�洢ʱ��
	private String type;//�洢���
	private String handler;//�洢������
	private String mark;//�洢���뱸ע
	public My_inaccount()
	{
		super();
	}
	//��ʼ��������Ϣʵ�����еĸ����ֶ�
	public My_inaccount(int id,double money,String time,String type,String handler,String mark)
	{
		super();
		this._id = id;//Ϊ�����Ÿ�ֵ
		this.money = money;
		this.time = time;
		this.type = type;
		this.handler = handler;
		this.mark = mark;
	}
	//ʹ�����ſɶ�
	public int getid()
	{
		return _id;
	}
	//ʹ�����ſ�д
	public void setid(int id)
	{
		this._id = id;
	}
	//ʹ����Ǯ���ɶ�
	public double getMoney()
	{
		return money;
	}
	//ʹ����Ǯ����д
	public void setMoney(double money)
	{
		this.money = money;
	}
	//ʹ����ʱ��ɶ�
	public String getTime()
	{
		return time;
	}
	//ʹ����ʱ���д
	public void setTime(String time)
	{
		this.time = time;
	}
	//ʹ�������ɶ�
	public String getType()
	{
		return type;
	}
	//ʹ��������д
	public void setType(String type)
	{
		this.type = type;
	}
	//ʹ���븶���˿ɶ�
	public String getHandler()
	{
		return handler;
	}
	//ʹ���븶���˿�д
	public void setHandler(String handler)
	{
		this.handler = handler;
	}
	//ʹ���뱸ע�ɶ�
	public String getMark()
	{
		return mark;
	}
	//ʹ���뱸ע��д
	public void setMark(String mark)
	{
		this.mark = mark;
	}
}