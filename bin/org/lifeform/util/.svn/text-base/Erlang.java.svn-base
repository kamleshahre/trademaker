package org.lifeform.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.ericsson.otp.erlang.OtpErlangAtom;
import com.ericsson.otp.erlang.OtpErlangDecodeException;
import com.ericsson.otp.erlang.OtpErlangExit;
import com.ericsson.otp.erlang.OtpErlangList;
import com.ericsson.otp.erlang.OtpErlangObject;
import com.ericsson.otp.erlang.OtpErlangTuple;
import com.ericsson.otp.erlang.OtpMbox;
import com.ericsson.otp.erlang.OtpNode;

public class Erlang {

	public static void send(final InputStream input) throws IOException, OtpErlangExit, OtpErlangDecodeException{
		OtpNode node = new OtpNode("java");
        OtpMbox mbox = node.createMbox("admin_gui");
        BufferedReader in = new BufferedReader(new InputStreamReader(input));
        String defaultServerNodeName = "erl_node@" + node.host();
        System.out.format("Server Node to contact [%s]> ", defaultServerNodeName);
        String serverNodeName = in.readLine();
        if (serverNodeName == null || "".equals(serverNodeName)) {
            serverNodeName = defaultServerNodeName;
        }
        OtpErlangTuple serverPidTuple = new OtpErlangTuple(new OtpErlangObject[] {
                new OtpErlangAtom("server"), new OtpErlangAtom(serverNodeName)});
        while (true) {
            if (!node.ping(serverNodeName, 1000)) {
                System.out.println("Erlang node is not available: " + serverNodeName);
                System.exit(1);
            }
            System.out.print("Message (Hit Enter to send)> ");
            String message = in.readLine();
            if (message != null) {
                mbox.send("server", serverNodeName, new OtpErlangTuple(
                        new OtpErlangObject[] {
                                mbox.self(), new OtpErlangList(message)}));
                OtpErlangObject serverReply = mbox.receive(1000);
                if (serverReply == null) {
                    System.out.println("WARN: Timeout when receiving reply");
                } else {
                    System.out.format("%s replied : %s%n", serverPidTuple, serverReply);
                }
            }
        }

	}
}
