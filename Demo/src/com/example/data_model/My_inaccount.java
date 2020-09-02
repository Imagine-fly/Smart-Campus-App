/**
 * 
 */
/**
 * @author 张朋
 *
 */
package com.example.data_model;
public class My_inaccount{
	private int _id;//存储收入编号
	private double money;//存储收入金额
	private String time;//存储时间
	private String type;//存储类别
	private String handler;//存储付款人
	private String mark;//存储收入备注
	public My_inaccount()
	{
		super();
	}
	//初始化收入信息实体类中的各个字段
	public My_inaccount(int id,double money,String time,String type,String handler,String mark)
	{
		super();
		this._id = id;//为收入编号赋值
		this.money = money;
		this.time = time;
		this.type = type;
		this.handler = handler;
		this.mark = mark;
	}
	//使收入编号可读
	public int getid()
	{
		return _id;
	}
	//使收入编号可写
	public void setid(int id)
	{
		this._id = id;
	}
	//使收入钱数可读
	public double getMoney()
	{
		return money;
	}
	//使收入钱数可写
	public void setMoney(double money)
	{
		this.money = money;
	}
	//使收入时间可读
	public String getTime()
	{
		return time;
	}
	//使收入时间可写
	public void setTime(String time)
	{
		this.time = time;
	}
	//使收入类别可读
	public String getType()
	{
		return type;
	}
	//使收入类别可写
	public void setType(String type)
	{
		this.type = type;
	}
	//使收入付款人可读
	public String getHandler()
	{
		return handler;
	}
	//使收入付款人可写
	public void setHandler(String handler)
	{
		this.handler = handler;
	}
	//使收入备注可读
	public String getMark()
	{
		return mark;
	}
	//使收入备注可写
	public void setMark(String mark)
	{
		this.mark = mark;
	}
}