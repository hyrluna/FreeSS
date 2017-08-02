package xh.freess;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by G1494458 on 2017/7/25.
 */

public class NetworkRequest {

    public static final int SITE_ISHADOWX = 1;
    public static final int SITE_FREESSR = 2;
    public static final String SITE_HOST_ISHADOW = "ss.ishadowx.com";
    public static final String SITE_HOST_FREE_SSR = "freessr.xyz";

    private static final String DOMAIN_ISHADOW = "http://ss.ishadowx.com/";
    private static final String DOMAIN_FREE_SSR = "https://freessr.xyz/";

    public Observable<FreeSSAccount> getIshadowFreeAccounts() {
        return Observable.just(DOMAIN_ISHADOW)
                .flatMap(new Function<String, ObservableSource<Element>>() {
                    @Override
                    public ObservableSource<Element> apply(String url) throws Exception {
                        Document doc = Jsoup.connect(url)
                                .userAgent("Mozilla")
                                .get();
                        Elements uls = doc.select("div.portfolio-item");
                        return Observable.fromIterable(uls);
                    }
                })
                .flatMap(new Function<Element, ObservableSource<FreeSSAccount>>() {
                    @Override
                    public ObservableSource<FreeSSAccount> apply(Element element) throws Exception {
                        Elements els = element.select("h4");
                        FreeSSAccount account = new FreeSSAccount();

                        String accountText = els.get(0).text();
                        account.setProxyServer(accountText.substring(accountText.indexOf(":") + 1).trim());

                        String portText = els.get(1).text();
                        account.setPort(portText.substring(portText.indexOf("：") + 1).trim());

                        String pwdText = els.get(2).text();
                        account.setPassword(pwdText.substring(pwdText.indexOf(":") + 1).trim());

                        String methodText = els.get(3).text();
                        account.setMethod(methodText.substring(methodText.indexOf(":") + 1).trim());

                        Element e = els.get(4).select("a").first();
                        String qrcode = "";
                        if (e != null) {
                            qrcode = e.absUrl("href");
                        }
                        account.setQrcode(qrcode);

                        String bg = element.select("img").first().absUrl("src");
                        account.setItemBg(bg);

                        return Observable.just(account);
                    }
                })
                .filter(new Predicate<FreeSSAccount>() {
                    @Override
                    public boolean test(FreeSSAccount account) throws Exception {
                        return !account.getPassword().equals("");
                    }
                });
    }

    public Observable<FreeSSAccount> getFreeSSRSiteAccount() {
        return Observable.just(DOMAIN_FREE_SSR)
                .flatMap(new Function<String, ObservableSource<Element>>() {
                    @Override
                    public ObservableSource<Element> apply(String url) throws Exception {
                        Document doc = Jsoup.connect(url)
                                .userAgent("Mozilla")
                                .get();
                        Elements els = doc.select("div.col-md-6");
                        els.remove(els.size() - 1);
                        return Observable.fromIterable(els);
                    }
                })
                .flatMap(new Function<Element, ObservableSource<FreeSSAccount>>() {
                    @Override
                    public ObservableSource<FreeSSAccount> apply(Element element) throws Exception {
                        Elements els = element.select("h4");
                        FreeSSAccount account = new FreeSSAccount();

                        //Server
                        String accountText = els.get(0).text();
                        account.setProxyServer(accountText.substring(accountText.indexOf(":") + 1).trim());

                        //Port
                        String portText = els.get(1).text();
                        account.setPort(portText.substring(portText.indexOf(":") + 1).trim());

                        //Password
                        String pwdText = els.get(2).text();
                        account.setPassword(pwdText.substring(pwdText.indexOf(":") + 1).trim());

                        //Method
                        String methodText = els.get(3).text();
                        account.setMethod(methodText.substring(methodText.indexOf(":") + 1).trim());

//                        Element e = els.get(4).select("a").first();
//                        String qrcode = "";
//                        if (e != null) {
//                            qrcode = e.absUrl("href");
//                        }
//                        account.setQrcode(qrcode);
//
//                        String bg = element.select("img").first().absUrl("src");
//                        account.setItemBg(bg);

                        return Observable.just(account);
                    }
                });
    }

}
