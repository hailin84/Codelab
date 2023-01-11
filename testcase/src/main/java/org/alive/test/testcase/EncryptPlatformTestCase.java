package org.alive.test.testcase;

import org.alive.test.core.TestCase;

/**
 * 测试加密平台相关API
 * 
 * @author hailin84
 * 
 * @date 2016.06.18
 * 
 */
public class EncryptPlatformTestCase extends TestCase {

	/** 加解密用到的初始化向量(32个0)，根据PBOC3.0规范定义 */
	private static final String iv = "00000000000000000000000000000000";

	/** 10.14.29.232/11118 */
//	UnionEsscAPI esscApi = null;
//	UnionEsscAPI longApi = null;

	String kek = "PA.GSS_SGE_KEK1.zmk";

	String kenc = "PA.GSS_SGE_ENC1.zek";

	String kmac = "PA.GSS_SGE_MAC1.zak";

	String kek_decrypt = "PA.GSS_SGE_KEK1.zek";

	String kek_mac = "PA.GSS_SGE_KEK1.zak";

//	TUnionTransInfo transInfo = null;

	public EncryptPlatformTestCase() {
		super();
	}

	public EncryptPlatformTestCase(String name) {
		super(name);
	}

//	public void setUp() {
//		List<String> ip = new ArrayList<String>();
//		List<Integer> port = new ArrayList<Integer>();
//		ip.add("10.14.29.232");
//		port.add(11118);
//		esscApi = new UnionEsscAPI(ip, port, 5, "916386", "IGTP-GSS", "1");
//		longApi = new UnionEsscAPI(ip, port, 5, "916386", "IGTP-GSS", 5, "1");
//		// System.out.println(esscApi);
//
//		// esscApi = new UnionEsscAPI(ip, port, 7, "shns", "shns");
//	}
//
//	public void tearDown() {
//	}
//
//	protected void queryKeyStatus(String keyName) {
//		// 获取对称密钥信息
//		transInfo = esscApi.unionAPIServiceE117(keyName);
//		System.out.println(transInfo.getResponseRemark());
//		System.out.println(transInfo.getLog());
//	}
//
//	/**
//	 * 生成对称密钥
//	 * 
//	 * @param keyName
//	 */
//	protected void generateKey(String keyName) {
//		// 生成对称密钥unionAPIServiceE110
//		transInfo = esscApi.unionAPIServiceE110(keyName, "1", "SM4", "MK-SMC", 128, 0, 1, 1, 1, 36500, 1, 1, "", "", 1,
//				1, 1, "");
//		System.out.println(transInfo.getResponseRemark());
//		System.out.println(transInfo.getLog());
//
//		if (transInfo.getIsSuccess() == 1) {
//			System.out.println(transInfo.getReturnBody().getKeyValue());
//			System.out.println(transInfo.getReturnBody().getCheckValue());
//		}
//	}
//
//	protected void setKey(String keyName, String keyValue, String kcv) {
//		transInfo = esscApi.unionAPIServiceE112(keyName, keyValue, kcv, "1", kek, -1);
//		System.out.println(transInfo.getResponseRemark());
//		System.out.println(transInfo.getLog());
//	}
//
//	/**
//	 * 启用或者停用KEY
//	 * 
//	 * @param keyName
//	 * @param mode
//	 *            1:启用；2:停用
//	 */
//	protected void activateKey(String keyName, int mode) {
//		transInfo = esscApi.unionAPIServiceE119(keyName, mode);
//		System.out.println(transInfo.getResponseRemark());
//		System.out.println(transInfo.getLog());
//	}
//
//	protected void exportKey(String keyName) {
//		// 导出对称密钥，用KEY保护
//		transInfo = esscApi.unionAPIServiceE113(keyName, "1", kek);
//		System.out.println(transInfo.getResponseRemark());
//		System.out.println(transInfo.getResponseCode());
//		System.out.println(transInfo.getLog());
//		if (transInfo.getIsSuccess() == 1) {
//			System.out.println(transInfo.getReturnBody().getKeyValue());
//			System.out.println(transInfo.getReturnBody().getCheckValue());
//		}
//	}
//
//	protected void destroyKey(String keyName) {
//		transInfo = esscApi.unionAPIServiceE116(keyName);
//		System.out.println(transInfo.getResponseRemark());
//		System.out.println(transInfo.getResponseCode());
//		System.out.println(transInfo.getLog());
//	}
//
//	protected String encryptData(String keyName, String data) {
//		String result = null;
//		transInfo = esscApi.unionAPIServiceE160(1, keyName, "", 0, 0, data, iv, 3);
//		System.out.println(transInfo.getResponseRemark());
//		System.out.println(transInfo.getResponseCode());
//		if (transInfo.getIsSuccess() == 1) {
//			result = transInfo.getReturnBody().getData();
//			System.out.println(result);
//		}
//
//		return result;
//	}
//
//	protected String decryptData(String keyName, String data) {
//		String result = null;
//		// TUnionTransInfo transInfo = esscApi.unionAPIServiceE161(1, keyName,
//		// "", 0, 1, data, iv, 0);
//		// TUnionTransInfo transInfo = esscApi.unionAPIServiceE161(1, keyName,
//		// "", 1, 0, data, iv, 3);
//		transInfo = esscApi.unionAPIServiceE161(1, keyName, "", 0, 0, data, iv, 3);
//		System.out.println(transInfo.getResponseRemark());
//		// System.out.println(transInfo.getResponseCode());
//		if (transInfo.getIsSuccess() == 1) {
//			result = transInfo.getReturnBody().getData();
//			System.out.println(result);
//		}
//		return result;
//	}
//
//	protected void validatedMac(String kmacName, String data, String mac) {
//		transInfo = esscApi.unionAPIServiceE151(1, kmacName, "", 1, 2, 1, data, mac);
//		System.out.println(transInfo.getResponseRemark());
//		System.out.println(transInfo.getResponseCode());
//	}
//
//	/**
//	 * 使用指定的密钥解密数据，可以是使用KEK解密GC01(isSpecial为true)，也可以是使用KENC解密业务报文(
//	 * isSpcial为false)
//	 * 
//	 * @param keyName
//	 * @param data
//	 * @param special
//	 * @return
//	 */
//	public String decrypt(String keyName, String data, boolean special) {
//		StringBuffer logMSg = new StringBuffer();
//		logMSg.append("解密数据: keyName=").append(keyName);
//		// logMSg.append(",ATC=").append(atc);
//
//		String logPattern = "解密数据: keyName={0}, \n密文={1}\n明文={2}\n结果={3}";
//		String msg = null;
//
//		String result = null;
//		TUnionTransInfo transInfo = null;
//		if (special) {
//			transInfo = esscApi.unionAPIServiceE161(1, keyName, "", 0, 1, data, iv, 0);
//		} else {
//			// transInfo = esscApi.unionAPIServiceE161(1, keyName, "", 0, 0,
//			// data, iv, 3);
//			transInfo = esscApi.unionAPIServiceE161(1, keyName, "", 0, 0, data, iv, 0);
//		}
//		if (transInfo.getIsSuccess() == 1) {
//			msg = "成功";
//			result = transInfo.getReturnBody().getData();
//		} else {
//			msg = "失败: " + transInfo.getResponseRemark();
//			// EMPLog.log(SHGEBConstant.SHGEB_MESSAGE, EMPLog.ERROR, 0,
//			// "使用[" + keyName + "]解密数据失败：" + transInfo.getResponseRemark());
//		}
//
//		System.out.println(MessageFormat.format(logPattern, new Object[] { keyName, data, result, msg }));
//		return result;
//	}
//
//	/**
//	 * 使用指定的密钥加密报文,可以是使用KEK加密GC01(isSpecial为true)，也可以是使用KENC加密业务报文(
//	 * isSpcial为false)
//	 * 
//	 * <ol>
//	 * <li>key为KEK，special为true：加密GC01，只加密业务报文中一部分，长度固定，不需要填充0x80 0x00；</li>
//	 * <li>key为KENC，special为false: 业务报文，加密整个报文，长度不固定，需要填充；</li>
//	 * </ol>
//	 * 
//	 * @param keyName
//	 * @param data
//	 * @param special
//	 * @return
//	 */
//	public String encrypt(String keyName, String data, boolean special) {
//		String logPattern = "加密数据: keyName={0}, \n明文={1}\n密文={2}\n结果={3}";
//		String msg = null;
//		TUnionTransInfo transInfo = null;
//		int len = data.getBytes().length;
//		// 报文字节长度为16的整数，加解密是不需要填充，format上送0；否则需要填充0x80，format上送3
//		int format = (len % 16 == 0) ? 0 : 3;
//		if (special) {
//			// GC01: 只加密业务报文中一部分，长度固定，不需要填充0x80 0x00
//			transInfo = esscApi.unionAPIServiceE160(1, keyName, "", 0, 1, data, iv, format);
//		} else {
//			// 业务报文，加密整个报文，长度不固定，需要填充
//			transInfo = esscApi.unionAPIServiceE160(1, keyName, "", 0, 0, data, iv, format);
//		}
//		String result = null;
//		if (transInfo.getIsSuccess() == 1) {
//			result = transInfo.getReturnBody().getData();
//			msg = "成功";
//		} else {
//			msg = "失败: " + transInfo.getResponseRemark();
//			// EMPLog.log(SHGEBConstant.SHGEB_MESSAGE, EMPLog.ERROR, 0,
//			// "使用[" + keyName + "]加密数据失败：" + transInfo.getResponseRemark());
//		}
//		System.out.println(MessageFormat.format(logPattern, new Object[] { keyName, data, result, msg }));
//
//		return result;
//	}
//
//	/**
//	 * 使用指定的密钥给数据生成MAC，协商会话密钥GC01的MAC值由KEK生成，其他的都有KMAC生成
//	 * 
//	 * @param keyName
//	 * @param data
//	 * @return
//	 */
//	public String generateMac(String keyName, String data, String atc) {
//		String result = null;
//		// TUnionTransInfo transInfo = esscApi.unionAPIServiceE150(1, keyName,
//		// "", 1, 2, 1, data);
//		// ATC为0x00 0x01，则上送0001
//		String logPattern = "生成MAC: keyName={0}, ATC={1} -- 结果: {2}";
//		String msg = null;
//
//		TUnionTransInfo transInfo = esscApi.unionAPIServiceE801(keyName, 1, 0, "", atc, data, iv);
//		if (transInfo.getIsSuccess() == 1) {
//			result = transInfo.getReturnBody().getMac();
//			msg = "生成MAC成功，MAC值为[" + result + "]";
//		} else {
//			msg = "生成MAC失败: " + transInfo.getResponseRemark();
//		}
//
//		System.out.println(MessageFormat.format(logPattern, new Object[] { keyName, atc, msg }));
//
//		return result;
//	}
//
//	/**
//	 * 使用指定的密钥验证MAC
//	 * 
//	 * @param keyName
//	 * @param data
//	 * @param mac
//	 * @return
//	 */
//	public boolean validateMac(String keyName, String data, String mac, String atc) {
//		String gmac = generateMac(keyName, data, atc);
//		boolean result = gmac != null && gmac.equals(mac);
//		System.out.println(MessageFormat.format("验证MAC: keyName={0}, ATC={1}, 原MAC={2}, 新MAC={3} -- 结果{4}",
//				new Object[] { keyName, atc, mac, gmac, result }));
//		return result;
//	}
//
//	public String digest(String data) {
//		String result = null;
//		String msg = null;
//		TUnionTransInfo transInfo = esscApi.unionAPIServiceE180(data, 1, 4);
//		if (transInfo.getIsSuccess() == 1) {
//			// result = transInfo.getReturnBody().getMac();
//			msg = "生成MAC成功，MAC值为[" + result + "]";
//		} else {
//			msg = "生成MAC失败: " + transInfo.getResponseRemark();
//		}
//		return "";
//	}
//
//	public String generateFileMac(String keyName, String data) {
//		String result = null;
//		String digest = null;
//		String msg = null;
//		String fileAtc = "0001";
//		String logPattern = "生成文件MAC: keyName={0}, ATC={1}，摘要={2}，MAC={3} -- 结果：{4}";
//		// 调用E180生成摘要，使用SM3摘要算法
//		TUnionTransInfo transInfo = esscApi.unionAPIServiceE180(data, 1, 4);
//		if (transInfo.getIsSuccess() == 1) {
//			digest = transInfo.getReturnBody().getData();
//			msg = "生成摘要成功" + digest;
//		} else {
//			msg = "生成摘要失败：" + transInfo.getResponseRemark();
//		}
//
//		// String mac = generateMac(keyName, result);
//
//		// 调用E801对摘要生成MAC，使用的ATC为fileATC，也即0001
//		if (digest != null) {
//			transInfo = esscApi.unionAPIServiceE801(keyName, 1, 0, "", fileAtc, digest, iv);
//			if (transInfo.getIsSuccess() == 1) {
//				result = transInfo.getReturnBody().getMac();
//				msg += "；生成文件MAC成功";
//			} else {
//				msg = "；生成文件MAC失败：" + transInfo.getResponseRemark();
//			}
//		}
//
//		System.out.println(MessageFormat.format(logPattern, new Object[] { keyName, fileAtc, digest, result, msg }));
//
//		return result;
//	}
//
//	public void testExport() {
//		//exportKey(kenc);
//		//exportKey(kmac);
//	}
//
//	public void mtestReceive() {
//		//
//
//		String msg = "58F33D2D2BA3E71A7B62F405DCDEF18DAACB10D47641D2704B5BB8AE1109ED1E46809E94" +
//				"735FDB6AFEC28F0EA7FA82FF|58B68C2CA0A40940C7DD000F999795FB"
//				;
//		String[] parts = msg.split("\\|");
//		String data = parts[0];
//		String mac = parts[1];
//		validateMac(kmac, data, mac, "000B");
//		String plain = decrypt(kenc, data, false);
//		String cipherData = encrypt(kenc, plain, false);
//	}
//
//	public void mtestSend() {
//		String data = "SHAU|CX01|16051716203510600000285|106|20160517||";
//		String cipherData = encrypt(kenc, data, false);
//		String mac = generateMac(kmac, cipherData, "0006");
//		String plain = decrypt(kenc, cipherData, false);
//	}
//
//	public void mtestFileMac() {
//		String file = "d:/myumen/Temp/data.txt";
//		String data = getFileContent(file);
//		// String data = "abcdefg";
//		generateFileMac(kek_mac, data);
//	}
//
//	public void testApi() throws Exception {
//		// this.activateKey(kmac, 1);
//		// this.generateKey(kmac);
//		// destroyKey(kmac);
//		// queryKeyStatus(kmac);
//		// /generateKey(kmac);
//		// exportKey(kek);
//		// exportKey(kenc);
//		// exportKey(kmac);
//
//		// System.out.println("原始数据：" + data);
//		// String result = encryptData(kenc, data);
//		// System.out.println("加密后：" + result);
//		// result = decryptData(kenc, result);
//		// System.out.println("解密后：" + result);
//		// String mac = generateMac(kmac, data); //
//		// BEBA04179DFA2167A5ED06AA618A9A64
//		//
//		// System.out.println(mac);
//		//
//		// validatedMac(kmac, data, mac);
//
//		// KEK分量：
//		// 分量一： 77777777777777777777777777777777
//		// 分量二： 00000000000000000000000000000000
//		// 手工录入到加密机
//
//		// setKey(kenc, "D4ED6C84662756937F31FF5C8F717594",
//		// "4F4EE5A3C54757AD2AB9C7352E065439".substring(0, 16));
//		// exportKey(kenc);
//
//		// setKey(kmac, "D30973852EF33178FB79DE2A4EA81FD1",
//		// "D988B0EEB652292C822385B80B721622".substring(0, 16));
//		// exportKey(kmac);
//
//		// data =
//		// "1277F15BF77A862E50D459A7785987801CC0B5D543C0B3B28602FDD644433BFA04262F5E16462DC2989E816F31A4F38FF0D725D39C5A6242B598FB1F4DEAC9D142723AB56D4E870690F3D372D797BC3960D7C6DC9894CD51FC390BFCE543DC1A";
//		// String mac = "DCCB6B5595D211BEEA65814A19090974";
//
//		// String newMac = generateMac(kmac, data);
//		// System.out.println("new mac: " + newMac);
//		// validatedMac(kek, data, mac);
//		// String result = decryptData(kek_decrypt, data);
//
//		// String plain =
//		// "3030303030300000000000000000000000000000323031363037303630303031D4ED6C84662756937F31FF5C8F7175944F4EE5A3C54757AD2AB9C7352E065439D30973852EF33178FB79DE2A4EA81FD1D988B0EEB652292C822385B80B721622";
//		// encryptData(kek, plain);;
//
//		// Test GC01
//
//		// String source =
//		// "4F0C830FF449C8D6F7F6A6F7EC91603328B4B6489EF02CCFBF9A3A175BBB95CC5E39F58492087A6E1DBF5E7662D999154814646DD8777F70960AD052C84E5ED3F34772E8625E6C39ABFE93AEFBDB2E01A5CC7466172617E6150E0474637099CFA40DFB298A1A378F0C7D699546EA53EA";
//		//
//		// String keyData = source.substring(0, 192);
//		// String keyMac = source.substring(192);
//		//
//		// String mac = generateMac(kek_mac, keyData, "0001");
//		// System.out.println("data=" + keyData);
//		// System.out.println("keyMac=" + keyMac);
//		// System.out.println("generated mac=" + mac);
//		//
//		// String plainData = decryptData(kek_decrypt, keyData);
//		// System.out.println("解密后数据：" + plainData);
//		//
//		// String chipherData = encryptData(kek_decrypt, plainData);
//		// System.out.println("加密：" + chipherData);
//
//		// String dataV = encryptData(kenc, data);
//		// generateMac(kmac, dataV);
//
//		// Test message
//		String data = "SHAU|HZ02|17011809563210600000003|106|001011||000000001011|19009888081501|19010259886201|RMB|300|20170118||";
//		String mac = null;
//		
//		this.encrypt(kenc, data, false);
//
//		// String newMac = generateMac(kmac, data, "0441"); // 注意这笔数据用的ATC为0441
//		// System.out.println("mac: " + mac);
//		// System.out.println("newMac: " + newMac);
//		// System.out.println(decryptData(kenc, data));
//
//		// String source = "SHAU|QD11|16051313371510600000106|RSP000000|abc|";
//		// String cipheredData = encrypt(kenc, source, false);
//		// System.out.println(cipheredData);
//		// System.out.println(decrypt(kenc, cipheredData, false));
//		//
//		// String atc = "0177";
//		// String newMac = generateMac(kmac, cipheredData, atc);
//		// System.out.println("new mac:" + newMac);
//	}
//
//	public void mtestE120() {
//		TUnionTransInfo transInfo = longApi.unionAPIServiceE120(kek, "1", "SM2", "1", 65537, 512, 0, 0, "", "01", 0, 1,
//				1, 1, 1, 36500, 2, 20011230, "", "");
//		System.out.println(transInfo.getResponseRemark());
//		System.out.println(transInfo.getResponseCode());
//		System.out.println(transInfo.getReturnBody().getPkValue());
//		System.out.println(transInfo.getReturnBody().getVkValue());
//	}
//
//	public void mtestE110() {
//		// transInfo = longApi.unionAPIServiceE110("hk.test10005.zmk", "1",
//		// "DES",
//		// "ZMK", 128, 0, 1, 1, 1, 36500, 1, 1, "", "", 1, 1, 1,
//		// "AT.2.zmk");
//		// transInfo = esscApi.unionAPIServiceE110(kek, "shgeb", "SM4",
//		// "MK-SMC", 128, 1, 1,
//		// 1, 1, 365, 2, 0, "", "", 1, 1, 0, "AT.2.zmk");
//		transInfo = esscApi.unionAPIServiceE110(kenc, "1", "SM4", "MK-SMC", 128, 0, 1, 1, 1, 36500, 1, 1, "", "", 1, 1,
//				1, "");
//		System.out.println(transInfo.getResponseRemark());
//		System.out.println(transInfo.getLog());
//		// System.out.println(transInfo.getReturnBody().getKeyValue());
//		// System.out.println(transInfo.getReturnBody().getKeyValue2());
//		System.out.println(transInfo.getReturnBody().getCheckValue());
//	}
//
//	/**
//	 * 按GBK读取文件内容，转换为字符串，注意，因为文件一般不会太大，故全部读取进来也无妨，文件如果超过10M，需要打印WARNING日志
//	 * 
//	 * @param file
//	 * @return
//	 */
//	public static String getFileContent(String file) {
//		FileChannel channel = null;
//		FileInputStream fs = null;
//		try {
//			File f = new File(file);
//			long size = f.length();
//			if (size > 1024 * 1024 * 5) {
//				System.out.println("文件超过5M，大小为" + size);
//			}
//
//			fs = new FileInputStream(f);
//			channel = fs.getChannel();
//			ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
//			while ((channel.read(byteBuffer)) > 0) {
//			}
//			return new String(byteBuffer.array());
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				if (channel != null) {
//					channel.close();
//				}
//			} catch (IOException e) {
//				// e.printStackTrace();
//			}
//			try {
//				if (fs != null) {
//					fs.close();
//				}
//			} catch (IOException e) {
//				// e.printStackTrace();
//			}
//		}
//
//		return null;
//	}
}
