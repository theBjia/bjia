/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 * 
 *  提示：如何获取安全校验码和合作身份者id
 *  1.用您的签约支付宝账号登录支付宝网站(www.alipay.com)
 *  2.点击“商家服务”(https://b.alipay.com/order/myorder.htm)
 *  3.点击“查询合作者身份(pid)”、“查询安全校验码(key)”
 */

package com.alipay;

//
// 请参考 Android平台安全支付服务(msp)应用开发接口(4.2 RSA算法签名)部分，并使用压缩包中的openssl RSA密钥生成工具，生成一套RSA公私钥。
// 这里签名时，只需要使用生成的RSA私钥。
// Note: 为安全起见，使用RSA私钥进行签名的操作过程，应该尽量放到商家服务器端去进行。
public final class Keys {

	// 合作身份者id，以2088开头的16位纯数字 2088411623736485
	public static final String DEFAULT_PARTNER = "2088411867064629";

	// 收款支付宝账号
	public static final String DEFAULT_SELLER = "baifahui@163.com";

	// 商户私钥，自助生成
	public static final String PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALf2Qz+AbrMfXNUpv5ugZZmhnEkycQgPOWCCDgXb2jaMfdn0HymHKuYX230S8QPAKBCfL5FodtE4PpC2ibUwpZT+W704eVJWM9qWoeDbB8xykOx2uTPsDinlIlFTS9PWnY6rZD7kWCrDR5RRzEvbFBrG4HK8UQZePo6CL5Wf7rUNAgMBAAECgYBj7dH53LpFcvPqlxMOZsLKAY/z8wKTbjGsyvjzFcojd42lp4aF+HbFJpCEEJX7g5Y/8TjVs/lXDUZvNbj4Q8StjYlJuO4AjGcNOUlFWB6yMZ7qi4VxHZOwCJnWbcK9GucLReB32qy4X24M71AbVy3Lq5ubuSloXRvQvxT8+N5GAQJBAPJZbFv7BcHHoog2wnxB9NrRTmiiXMfy+nmkdlTSIFVJd7+s/7Q+Ex47T5SH8IR1ZNygRKvhxkwoprIFAsT1xfkCQQDCUusFSGJ7DkOPPBIMl/nDHeBAmVaaARWsit0gHvVzCBupA4uewMlvJ8sHYmzGRhjLtV6VZxIAVjzC5MNPNpy1AkEAgKVxfP4qlywHHjGbWeSVOtbivW56u/VCevIPkoUcfsmKFKib8C0HbwPjAaCLz3SVvvwgl/E4l1L48gTw7Gbe2QJAaKKvbFvKs4o9tK3a5i8bTQYUT5SBvlFhM1Ret0IUVomCI2MGVkhm7tSVjXdhLO5nwaD6uiYS2l2VQDhgNpIolQJBANy0w8mRUqzJ+KVKnavxN6guzZjGI0IDVK7JbkCMlES2o7TYRW7N7iyQ980ir0G3o2Odo+416mYSrcT/TBqpSmQ=";

	public static final String PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	// 合作身份者id，以2088开头的16位纯数字
	// 2088411623736485
	// public static final String DEFAULT_PARTNER = "2088411623736485";
	//
	// // 收款支付宝账号
	// public static final String DEFAULT_SELLER = "admin01@facebookchina.cn";
	//
	// // 商户私钥，自助生成
	// public static final String PRIVATE =
	// "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKnYz4XRwqCt4AmUqQUI7Ky9HKaT2SdIq19gogHcP4PRpHXDgioyxGyITsOfttmmHVt4g6tyfaad14z4Q1frWbroBozrkxHUrg0YJb/gIY/7FN+ldpdEv3Zn5XCjAAbrdzQylIRW/7HdKbOnGGEBlWJr6XjRlknUTOOMiruCvCAnAgMBAAECgYEAl3zmUy/szM1odoMeYXw9RDCEHlreIzigiTgyJ0Cfcd4JNnQDY8nWAgttXPPV7bhIlO4fWkqKoZJEz9oajo8+fo86ifHmLQYNWz4AA69OAryp1NsvTc9TzeMcNHXHRY2WOk36w5Hof1HrlnaksNQiD8Esnrd3PM+Xc3MVZiHc7mECQQDSbVCh+psclBL4WjmXPgbSFHobxd2JlPuqoY8hr1tS+JbYuWeImZDpSq7nPtnHLtp1Rkd1mc4kl7VFzjqaqhxRAkEAzqGeBTrMQGEhgzyeuK74x51T/j96aaRAv+R7cpwRXEI6uMdd77/LnnF/HTMvYPaX/v35LpZxIv2OwZs3vXBu9wJAAzu4xHRkU8EWtYg1YFSMqEe2RJz6tXV7lHidCWh3X8QAFV5r79ZkOqFyJI17rSrUIn8s8+qkFz3ZhDQSBHResQJBAIqHXaqB5eePdulGHd8RJyakemgs5IOoo+RCaOTIbvccS8QPnRGb0wYRbzgDVdM632ILoSAzqfvcY/W2eyMdGrcCQEGGfPbqOvXAIdphCZ19CARwHjk5eWCK4eV2/q1/AE188Nvu0b4ILGRMl50epY8lkQa0LseYQbHQCRf0BA0DJk0=";
	//
	// public static final String PUBLIC =
	// "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCp2M+F0cKgreAJlKkFCOysvRymk9knSKtfYKIB3D+D0aR1w4IqMsRsiE7Dn7bZph1beIOrcn2mndeM+ENX61m66AaM65MR1K4NGCW/4CGP+xTfpXaXRL92Z+VwowAG63c0MpSEVv+x3SmzpxhhAZVia+l40ZZJ1EzjjIq7grwgJwIDAQAB";
}
