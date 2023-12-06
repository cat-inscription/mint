import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.utils.Convert;
import java.io.IOException;
import java.math.BigInteger;

public class MintCat {
	public static void main(String[] args) {
		// 填写网络节点API 默认公共节点
		String api = "https://polygon-rpc.com";
		// 填写链ID MATIC链是137
		long chainId = 137L;
		// 钱包私钥
		String key = "";
		// 循环次数 默认100次
		int num = 100;
		// 铭文数据 默认CAT
		String data = "0x646174613a2c7b2261223a224e657874496e736372697074696f6e222c2270223a226f7072632d3230222c226f70223a226d696e74222c227469636b223a22434154222c22616d74223a22313030303030303030227d";
		// 创建连接对象
		Web3j web3 = Web3j.build(new HttpService(api));
		// 创建钱包对象
		Credentials credentials = Credentials.create(key);
		// 获取自己的钱包地址
		String address = credentials.getAddress();
		// 交易金额
		BigInteger amount = Convert.toWei("0", Convert.Unit.ETHER).toBigInteger();
		// 循环执行交易==打铭文
		for(int i = 0; i < num; i++){
			try {
				// 获取当前网络的建议费用
				BigInteger maxPriorityFeePerGas = web3.ethGasPrice().send().getGasPrice();
				BigInteger maxFeePerGas = maxPriorityFeePerGas.add(BigInteger.valueOf(1000000000L));// 加10Gwei
				// 构建交易对象
				EthSendTransaction ethSendTransaction = new RawTransactionManager(
						web3, credentials)
						.sendEIP1559Transaction(
								chainId,
								maxPriorityFeePerGas, // 优先级
								maxFeePerGas, // 最大费用
								BigInteger.valueOf(30000L),// gasLimit 手动调整
								address,
								data,
								amount
						);
				String transactionHash = ethSendTransaction.getTransactionHash();
				// 交易哈希
				System.out.println("【"+(num+1)+"】"+transactionHash);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
