/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/
package org.apache.james.mailbox.inmemory;

import org.apache.james.mailbox.MailboxSession;
import org.apache.james.mailbox.exception.MailboxException;
import org.apache.james.mailbox.exception.SubscriptionException;
import org.apache.james.mailbox.inmemory.mail.InMemoryMailboxMapper;
import org.apache.james.mailbox.inmemory.mail.InMemoryMessageMapper;
import org.apache.james.mailbox.inmemory.mail.InMemoryModSeqProvider;
import org.apache.james.mailbox.inmemory.mail.InMemoryUidProvider;
import org.apache.james.mailbox.inmemory.user.InMemorySubscriptionMapper;
import org.apache.james.mailbox.store.MailboxSessionMapperFactory;
import org.apache.james.mailbox.store.mail.MailboxMapper;
import org.apache.james.mailbox.store.mail.MessageMapper;
import org.apache.james.mailbox.store.user.SubscriptionMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import core.util.ProxySelectorRegex;

import java.lang.reflect.Method;
import java.net.Proxy;
import java.net.InetSocketAddress;

public class InMemoryMailboxSessionMapperFactory extends MailboxSessionMapperFactory<Long> {

    private MailboxMapper<Long> mailboxMapper;
    private MessageMapper<Long> messageMapper;
    private SubscriptionMapper subscriptionMapper;

    static Logger log = LoggerFactory.getLogger(InMemoryMailboxSessionMapperFactory.class);
    
    public InMemoryMailboxSessionMapperFactory() throws MailboxException
    {
        log.info("Installing proxy");
        ProxySelectorRegex proxySelector = new ProxySelectorRegex();
        proxySelector.excludeHosts(ProxySelectorRegex.DEFAULT_EXCLUDED_HOSTS);
        proxySelector.addProxy ("socket", new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("127.0.0.1", 23232)));
        proxySelector.install();
        log.info("Finished Installing proxy");
	
        mailboxMapper = new InMemoryMailboxMapper();
        messageMapper = new InMemoryMessageMapper(null, new InMemoryUidProvider(), new InMemoryModSeqProvider());
        subscriptionMapper = new InMemorySubscriptionMapper();
    }
    
    @Override
    public MailboxMapper<Long> createMailboxMapper(MailboxSession session) throws MailboxException {
        return mailboxMapper;
    }

    @Override
    public MessageMapper<Long> createMessageMapper(MailboxSession session) throws MailboxException {
        return messageMapper;
    }

    @Override
    public SubscriptionMapper createSubscriptionMapper(MailboxSession session) throws SubscriptionException {
        return subscriptionMapper;
    }

    public void deleteAll() throws MailboxException {
        ((InMemoryMailboxMapper) mailboxMapper).deleteAll();
        ((InMemoryMessageMapper) messageMapper).deleteAll();
        ((InMemorySubscriptionMapper) subscriptionMapper).deleteAll();
    }

}
