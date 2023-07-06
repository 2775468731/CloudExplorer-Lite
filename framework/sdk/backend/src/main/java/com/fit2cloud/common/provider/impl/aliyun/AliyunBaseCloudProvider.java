package com.fit2cloud.common.provider.impl.aliyun;

import com.fit2cloud.common.platform.credential.Credential;
import com.fit2cloud.common.platform.credential.impl.AliCredential;
import com.fit2cloud.common.provider.AbstractBaseCloudProvider;
import com.fit2cloud.common.provider.IBaseCloudProvider;
import com.fit2cloud.common.provider.entity.F2CBalance;
import com.fit2cloud.common.provider.impl.aliyun.api.AliyunBaseCloudApi;
import com.fit2cloud.common.provider.impl.aliyun.api.AliyunBaseMethodApi;
import com.fit2cloud.common.provider.impl.aliyun.entity.request.GetAccountBalanceRequest;
import com.fit2cloud.common.provider.impl.aliyun.entity.request.GetRegionsRequest;
import com.fit2cloud.common.utils.JsonUtil;
import org.pf4j.Extension;

import java.util.List;
import java.util.Map;


@Extension
public class AliyunBaseCloudProvider extends AbstractBaseCloudProvider<AliCredential> implements IBaseCloudProvider {

    private static final String logoSvg = "<svg  viewBox=\"0 0 160 38\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">\n" +
            "<path d=\"M104.206 25.7142H112.252V23.1999H104.206V20.6856H112V7.61133H93.8973V20.6856H101.692V23.1999H93.6459V25.7142H101.692V28.4799H93.0173V30.9942H113.006V28.4799H104.332V25.7142H104.206ZM104.457 10.1256H109.737V12.8913H104.457V10.1256ZM104.457 15.4056H109.737V18.0456H104.457V15.4056ZM101.943 18.0456H96.6631V15.4056H101.943V18.0456ZM101.943 12.8913H96.6631V10.1256H101.943V12.8913ZM37.2002 17.6685H50.4002V20.6856H37.2002V17.6685Z\" fill=\"#FF6A00\"/>\n" +
            "<path d=\"M56.9371 6.85718H48.2629L50.4 9.87432L56.6857 11.8857C57.8171 12.2629 58.5714 13.3943 58.5714 14.5257V24.08C58.5714 25.2115 57.8171 26.3429 56.6857 26.72L50.4 28.7315L48.2629 31.7486H56.9371C60.5829 31.7486 63.4743 28.8572 63.4743 25.2115V13.52C63.4743 9.87432 60.5829 6.85718 56.9371 6.85718ZM30.7886 26.72C29.6571 26.3429 28.9029 25.2115 28.9029 24.08V14.5257C28.9029 13.3943 29.6571 12.2629 30.7886 11.8857L37.0743 9.87432L39.2114 6.85718H30.5371C26.8914 6.85718 24 9.87432 24 13.52V25.0858C24 28.7315 26.8914 31.6229 30.5371 31.6229H39.2114L37.0743 28.6058L30.7886 26.72ZM85.2229 12.8915H78.8114V26.0915H85.2229V12.8915ZM82.5829 23.4515H81.2V15.4057H82.5829V23.4515ZM69.3829 30.9943H71.8971V10.1257H74.5371L73.0286 17.04V19.5543H74.5371V25.0858C74.5371 25.4629 74.2857 25.7143 73.9086 25.7143H73.28V28.2286H74.5371C75.92 28.2286 77.0514 27.0972 77.0514 25.7143V16.9143H75.5429L77.0514 10V7.48575H69.3829V30.9943Z\" fill=\"#FF6A00\"/>\n" +
            "<path d=\"M78.1826 10.1256H86.8569V26.3428C86.8569 27.4742 85.9769 28.4799 84.7198 28.4799H82.834V30.9942H85.474C87.6112 30.9942 89.4969 29.2342 89.4969 26.9713V10.1256H90.3769V7.61133H78.1826V10.1256ZM116.4 7.73704H134.503V10.2513H116.4V7.73704ZM135.508 19.6799V17.1656H115.52V19.6799H120.548L116.525 28.4799V30.9942H133.497C134.251 30.9942 134.754 30.3656 134.754 29.737C134.754 29.4856 134.754 29.3599 134.628 29.2342L132.491 24.457H129.725L131.611 28.4799H119.417L123.44 19.6799H135.508Z\" fill=\"#FF6A00\"/>\n" +
            "</svg>\n";

    private static final String iconSvg = "<svg t=\"1688528633663\" class=\"icon\" viewBox=\"0 0 1024 1024\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\" p-id=\"14077\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" width=\"100%\" height=\"100%\"><path d=\"M228.616 662.377c-18.254-4.236-32.293-19.763-32.293-39.526V405.455c1.404-19.764 14.041-35.291 32.293-39.527l200.78-43.76 21.062-86.112H217.384c-82.839 0-148.83 66.347-148.83 148.225v256.923c0 81.876 67.395 149.636 148.83 149.636h233.074l-21.061-86.112-200.781-42.351z m584.092-426.323H578.226l21.062 86.111 200.785 43.761c18.253 4.237 32.287 19.764 32.287 39.526V622.85c-1.4 19.763-14.035 35.29-32.287 39.526l-200.785 43.762-21.062 86.11h234.482c81.436 0 148.83-66.347 148.83-149.635V384.278c-1.41-81.877-67.394-148.224-148.83-148.224zM429.396 502.86H600.69v21.174H429.396V502.86z\" fill=\"#ff6c06\" p-id=\"14078\"></path></svg>";

    private static final CloudAccountMeta cloudAccountMeta = new CloudAccountMeta(AliCredential.class, "fit2cloud_ali_platform", "阿里云", true, logoSvg, iconSvg, Map.of());

    private static final Info info = new Info("management-center", List.of(), Map.of());

    @Override
    public F2CBalance getAccountBalance(String getAccountBalanceRequest) {
        return AliyunBaseCloudApi.getAccountBalance(JsonUtil.parseObject(getAccountBalanceRequest, GetAccountBalanceRequest.class));
    }

    @Override
    public CloudAccountMeta getCloudAccountMeta() {
        return cloudAccountMeta;
    }

    @Override
    public Info getInfo() {
        return info;
    }

    public List<Credential.Region> getRegions(String req) {
        return AliyunBaseMethodApi.getRegions(JsonUtil.parseObject(req, GetRegionsRequest.class));
    }
}
