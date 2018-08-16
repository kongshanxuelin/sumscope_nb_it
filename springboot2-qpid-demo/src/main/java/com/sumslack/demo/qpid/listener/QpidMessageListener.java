package com.sumslack.demo.qpid.listener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.qpid.client.AMQAnyDestination;
import org.apache.qpid.client.AMQConnection;

public class QpidMessageListener implements MessageListener {

	private String q_cc;
	private String q_cc_dest;
	private ConnectionFactory connectionFactory = null;
	private Connection connection;
	private Session session;
	private MessageProducer sender_cc;
	private List<String> qpid_uuid_list = new ArrayList<String>();

	
	private Destination dest_consumer;
	private Destination dest_cc;
	public static QpidMessageListener getInstance() {
		if (instance == null) {
			instance = new QpidMessageListener();
		}
		return instance;
	}

	private static QpidMessageListener instance = new QpidMessageListener();

	private QpidMessageListener() {
		//'com.sumslack.demo.qpid.listener.MyFailover'
		String url = "amqp://guest:guest@test/?failover='roundrobin?cyclecount='com.sumslack.demo.qpid.listener.MyFailover''&brokerlist='tcp://192.168.1.63:5672?connectdelay='2000'&retries='2147483647'&connecttimeout='3600000''";
		this.q_cc = "TT55";
		this.q_cc_dest = "TT55";

		System.out.println("start init qpid:" + url + ",q_cc:" + q_cc);
		try {
			 connectionFactory = new org.apache.qpid.client.AMQConnectionFactory(url);
			//connection = new AMQConnection(url);
			 connection = connectionFactory.createConnection("guest","guest");
			connection.start();
			connection.setExceptionListener(new ExceptionListener() {
				public void onException(JMSException ex) {
					//ex.printStackTrace();
					System.out.println("connection failover,reconnected..." + ex.getMessage());
				}
			});
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// direct 模式
			String addr_cc = "ADDR:" + this.q_cc + "; {create: always,node:{ type: queue }}";
			dest_cc = new AMQAnyDestination(addr_cc);
			sender_cc = session.createProducer(dest_cc);

			dest_consumer = new AMQAnyDestination("ADDR:" + this.q_cc_dest + "; {create: always,node:{ type: queue }}");
			session.createConsumer(dest_consumer).setMessageListener(this);

//			Destination dest_topic = new AMQAnyDestination("ADDR:" + this.q_cc_dest + "; {create: always,node:{ type: topic }}");
//			session.createTopic("TESTTOPIC");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(Object obj) {
		try {
			if (connection == null)
				return;
			//System.out.println("#####send data:" + obj.toString());
			
//			byte[] message = "hello,bytes".getBytes();
//			BytesMessage msg = session.createBytesMessage();
//            msg.setStringProperty("test","testtype");
//            msg.writeBytes(message);
//            sender_cc.send(dest_consumer,msg);
			
			MapMessage msg = session.createMapMessage();
			msg.setObject("msg", obj);
			String uuid =getUUID();
			msg.setStringProperty("x-amqp-0-10.app-id", uuid);
			System.out.println("#####MessageID:" + uuid+",sessin:"+session.toString());
			msg.setJMSReplyTo(new AMQAnyDestination(q_cc_dest));
			sender_cc.send(msg);
		} catch (Exception ex) {
			//ex.printStackTrace();
		}
	}

	public void onMessage(Message tempMsg) {
		try {
			System.out.println("#####Start to execute the listening method#####");
			if (tempMsg instanceof MapMessage) {
				MapMessage mapMessage = (MapMessage) tempMsg;
				String qpid_uuid = mapMessage.getStringProperty("x-amqp-0-10.app-id");
//				if(isExists(qpid_uuid)){
//					String msg = mapMessage.getObject("response").toString();
//					System.out.println("#####return data:" + msg);
//					removeUUID(qpid_uuid);
//				}else{
//					System.out.println("#####MessageID:'"+qpid_uuid+"' does not exist in the current queue."); 
//				}
				System.out.println("#####收到响应:"+qpid_uuid_list.toString());
			}
		} catch (Exception ex) {
			//ex.printStackTrace();
		}
	}

	public String getUUID() {
		String uuid = UUID.randomUUID().toString();
		qpid_uuid_list.add(uuid);
		return uuid;
	}

	public boolean removeUUID(String uuid) {
		return qpid_uuid_list.remove(uuid);
	}
	
	public boolean isExists(String uuid){
		return qpid_uuid_list.contains(uuid);
	}

	public static void main(String[] args) {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				QpidMessageListener.getInstance().sendMessage("hello,qpid:" + new Date().getTime());
				
			}
		};
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(task, 0, 5*1000);
	}
}