package com.fzy;

import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.productivity.java.syslog4j.Syslog;
import org.productivity.java.syslog4j.SyslogIF;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * @author fuzhongyu
 * @date 2018/1/8
 */

public class FlumeSysLog extends AbstractJavaSamplerClient implements Serializable {

    private final String str="<190>2016-10-12 03:43:03 TelinAC --AC/SSID/6/syslog(23): telnet; SSID; 412001; Station(ip='192.168.100.120'&mac='3C:32:02:28:4E:A2') 通过SSID(name='test_ssid_1'&vlan=12)";

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {

        SampleResult result=new SampleResult();
        result.sampleStart();
        sendLog("127.0.0.1",4444,str,6);
        result.sampleEnd();
        return result;
    }

    /**
     * syslog发送日志
     * @param host
     * @param port
     * @param log 日志内容
     * @param level  日志等级(severity)： 当超出7的时候level的值为  level%8
    0 LOG_EMERG：紧急情况，需要立即通知技术人员。
    1 LOG_ALERT：应该被立即改正的问题，如系统数据库被破坏，ISP连接丢失。
    2 LOG_CRIT：重要情况，如硬盘错误，备用连接丢失。
    3 LOG_ERR：错误，不是非常紧急，在一定时间内修复即可。
    4 LOG_WARNING：警告信息，不是错误，比如系统磁盘使用了85%等。
    5 LOG_NOTICE：不是错误情况，也不需要立即处理。
    6 LOG_INFO：情报信息，正常的系统消息，比如骚扰报告，带宽数据等，不需要处理。
    7 LOG_DEBUG：包含详细的开发情报的信息，通常只在调试一个程序时使用。
     */
    public void sendLog(String host,Integer port,String log,Integer level){

        try {
            SyslogIF syslogIF=Syslog.getInstance("udp");
            syslogIF.getConfig().setHost(host);
            syslogIF.getConfig().setPort(port);
            syslogIF.log(level,URLDecoder.decode(log,"utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
