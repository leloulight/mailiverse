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
package org.apache.james.mailbox.store.search.comparator;

import java.util.Comparator;

import org.apache.james.mailbox.store.mail.model.Message;

/**
 * {@link Comparator} which compares {@link Message}'s with their {@link Message#getInternalDate()} value
 *
 */
public class InternalDateComparator  implements Comparator<Message<?>>{


    private final static Comparator<Message<?>> INTERNALDATE = new InternalDateComparator();;
    private final static Comparator<Message<?>> REVERSE_INTERNALDATE = new ReverseComparator(INTERNALDATE);

    
    @Override
    public int compare(Message<?> o1, Message<?> o2) {
        return (o1.getInternalDate().compareTo(o2.getInternalDate()));
    }

    public static Comparator<Message<?>> internalDate(boolean reverse){
        if (reverse) {
            return REVERSE_INTERNALDATE;
        } else {
            return INTERNALDATE;
        }
    }
}