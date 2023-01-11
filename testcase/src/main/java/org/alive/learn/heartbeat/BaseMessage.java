package org.alive.learn.heartbeat;

import java.io.UnsupportedEncodingException;
import java.nio.channels.SocketChannel;

import org.alive.util.ByteTransUtil;

/**
 * 消息报文，格式为12字节报方头+消息UTF-8编码字节数组，12字节头部依次为3个整数：checkNum|length|messageType
 * 
 * @author hailin84
 * @since 2017.06.08
 */
public class BaseMessage {
	/** 消息头字节长度  */
	public static final int HEAD_LEN = 12;
	
	/** 业务报文 */
	public static final int MESSAGE_TYPE_BIZ = 1;

	/** 心跳报文 */
	public static final int MESSAGE_TYPE_HB = 2;

	public static final int DEFAULT_CHECKNUM = 16617743;

	public static final String ENCODING = "UTF-8";

	/** 设置一个随机数字，以防止通过telnet连接过来后随便输入 */
	private int checkNum = DEFAULT_CHECKNUM;

	/** 消息体长度，即messageBody.length */
	private int length = 0;

	/** 消息类型：1：业务消息；2：心跳消息 */
	private int messageType = MESSAGE_TYPE_BIZ;

	/** 消息头，12字节长度，依次由三个数字组成：checkNum|length|messageType，数字按大端存取 */
	private byte[] messageHead;

	/** 消息体，默认为UTF-8编码 */
	private byte[] messageBody;

	/** 消息读取的SocketChannel，对应的响应消息需要写回此SocketChannel */
	private SocketChannel channel;

	private ExtendSocketChannel extendChannel;

	/** 默认心跳报文，即12字节的报文头 */
	public static final BaseMessage HEATBEAT_MSG = buildHeartBeatMsg();

	public static BaseMessage buildHeartBeatMsg() {
		BaseMessage hb = new BaseMessage();
		hb.setMessageType(MESSAGE_TYPE_HB);
		return hb;
	}

	public BaseMessage() {
	}

	public BaseMessage(String msg) {
		if (msg == null || msg.length() == 0) {
			return;
		}
		try {
			this.messageBody = msg.getBytes(ENCODING);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.length = this.messageBody.length;
	}

	public BaseMessage(byte[] fullMsg) {
		if (fullMsg == null || fullMsg.length < 12) {
			return;
		}

		this.messageHead = new byte[12];
		System.arraycopy(fullMsg, 0, messageHead, 0, 12);
		this.parseHead();
		if (fullMsg.length > 12) {
			this.messageBody = new byte[this.length];
			System.arraycopy(fullMsg, 12, this.messageBody, 0, this.length);
		}
	}

	public int getCheckNum() {
		return checkNum;
	}

	public void setCheckNum(int checkNum) {
		this.checkNum = checkNum;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public byte[] getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(byte[] messageBody) {
		this.messageBody = messageBody;
		if (this.messageBody != null) {
			this.length = this.messageBody.length;
		}
	}

	public SocketChannel getChannel() {
		return channel;
	}

	public void setChannel(SocketChannel channel) {
		this.channel = channel;
	}

	public ExtendSocketChannel getExtendChannel() {
		return extendChannel;
	}

	public void setExtendChannel(ExtendSocketChannel extendChannel) {
		this.extendChannel = extendChannel;
	}

	public byte[] getMessageHead() {
		if (this.messageHead == null) {
			this.composeHead();
		}
		return messageHead;
	}

	public void setMessageHead(byte[] messageHead) {
		this.messageHead = messageHead;
		this.parseHead();
	}

	private void parseHead() {
		if (messageHead == null || messageHead.length != 12) {
			return;
		}
		byte[] tmps = new byte[4];
		System.arraycopy(messageHead, 0, tmps, 0, 4);
		this.checkNum = ByteTransUtil.byteArrayToInt(tmps, false);

		System.arraycopy(messageHead, 4, tmps, 0, 4);
		this.length = ByteTransUtil.byteArrayToInt(tmps, false);

		System.arraycopy(messageHead, 8, tmps, 0, 4);
		this.messageType = ByteTransUtil.byteArrayToInt(tmps, false);
	}

	private void composeHead() {
		this.messageHead = new byte[12];
		System.arraycopy(ByteTransUtil.intToByteArray(this.checkNum, false), 0, messageHead, 0, 4);
		System.arraycopy(ByteTransUtil.intToByteArray(this.length, false), 0, messageHead, 4, 4);
		System.arraycopy(ByteTransUtil.intToByteArray(this.messageType, false), 0, messageHead, 8, 4);
	}

	@Override
	public String toString() {
		return "BaseMessage [checkNum=" + checkNum + ", length=" + length + ", messageType=" + messageType + "]["
				+ bodyToString() + "]";
	}

	public String bodyToString() {
		String body = null;
		if (this.messageBody != null && this.messageBody.length > 0) {
			try {
				body = new String(messageBody, ENCODING);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				body = null;
			}
		}
		return body;
	}

	/**
	 * 生成完整消息对应的字节数组，如果没有消息体，就只有12字节头部
	 * 
	 * @return
	 */
	public byte[] composeFull() {
		if (this.messageBody != null) {
			this.length = this.messageBody.length;
		}

		byte[] data = new byte[this.length + 12];
		System.arraycopy(ByteTransUtil.intToByteArray(this.checkNum, false), 0, data, 0, 4);
		System.arraycopy(ByteTransUtil.intToByteArray(this.length, false), 0, data, 4, 4);
		System.arraycopy(ByteTransUtil.intToByteArray(this.messageType, false), 0, data, 8, 4);

		if (this.messageBody != null) {
			System.arraycopy(this.messageBody, 0, data, 12, this.length);
		}
		// FIXME: 完善代码
		return data;
	}
}
