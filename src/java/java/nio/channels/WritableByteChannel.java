/*
 * Copyright (c) 2000, 2005, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package java.nio.channels;

import java.io.IOException;
import java.nio.ByteBuffer;


/**
 * A channel that can write bytes.
 *
 * <p> Only one write operation upon a writable channel may be in progress at
 * any given time.  If one thread initiates a write operation upon a channel
 * then any other thread that attempts to initiate another write operation will
 * block until the first operation is complete.  Whether or not other kinds of
 * I/O operations may proceed concurrently with a write operation depends upon
 * the type of the channel. </p>
 *
 *
 * @author Mark Reinhold
 * @author JSR-51 Expert Group
 * @since 1.4
 */

public interface WritableByteChannel
    extends Channel
{

    /**
     * Writes a sequence of bytes to this channel from the given buffer.
     *
     * <p> An attempt is made to write up to <i>r</i> bytes to the channel,
     * where <i>r</i> is the number of bytes remaining in the buffer, that is,
     * <tt>src.remaining()</tt>, at the moment this method is invoked.
     *
     * <p> Suppose that a byte sequence of length <i>n</i> is written, where
     * <tt>0</tt>&nbsp;<tt>&lt;=</tt>&nbsp;<i>n</i>&nbsp;<tt>&lt;=</tt>&nbsp;<i>r</i>.
     * This byte sequence will be transferred from the buffer starting at index
     * <i>p</i>, where <i>p</i> is the buffer's position at the moment this
     * method is invoked; the index of the last byte written will be
     * <i>p</i>&nbsp;<tt>+</tt>&nbsp;<i>n</i>&nbsp;<tt>-</tt>&nbsp;<tt>1</tt>.
     * Upon return the buffer's position will be equal to
     * <i>p</i>&nbsp;<tt>+</tt>&nbsp;<i>n</i>; its limit will not have changed.
     *
     * <p> Unless otherwise specified, a write operation will return only after
     * writing all of the <i>r</i> requested bytes.  Some types of channels,
     * depending upon their state, may write only some of the bytes or possibly
     * none at all.  A socket channel in non-blocking mode, for example, cannot
     * write any more bytes than are free in the socket's output buffer.
     *
     * <p> This method may be invoked at any time.  If another thread has
     * already initiated a write operation upon this channel, however, then an
     * invocation of this method will block until the first operation is
     * complete. </p>
     *
     * @param  src
     *         The buffer from which bytes are to be retrieved
     *
     * @return The number of bytes written, possibly zero
     *
     * @throws  NonWritableChannelException
     *          If this channel was not opened for writing
     *
     * @throws  ClosedChannelException
     *          If this channel is closed
     *
     * @throws  AsynchronousCloseException
     *          If another thread closes this channel
     *          while the write operation is in progress
     *
     * @throws  ClosedByInterruptException
     *          If another thread interrupts the current thread
     *          while the write operation is in progress, thereby
     *          closing the channel and setting the current thread's
     *          interrupt status
     *
     * @throws  IOException
     *          If some other I/O error occurs
     */
    /**
     * 从给定的缓冲区将字节序列写入此通道。
     *
     * 尝试将最多r个字节写入通道，其中r是调用此方法时缓冲区中剩余的字节数，即src.remaining()。
     *
     * 假设写入长度为n的字节序列，其中0 <= n <= r。
     * 此字节序列将从索引p开始的缓冲区中转移，其中p是调用此方法时缓冲区的position。
     * 返回的最后一个字节的索引将为p+n-1。返回时，缓冲区的position将等于p + n；它的limit不会改变。
     *
     * 除非另有说明，否则仅在写入所有r个请求的字节之后，一个写入操作才会返回。
     * 根据其状态，某些类型的通道可能只写入某些字节，或者可能根本不写入。
     * 例如，处于非阻塞模式的socket通道不能写入的字节数要多于socket输出缓冲区中的可用字节数。
     *
     * 可以随时调用此方法。但是，如果另一个线程已经在该通道上启动了写操作，则该方法的调用将阻塞，直到第一个操作完成。
     *
     * @param src 要从中获取字节的缓冲区
     * @return 写入的字节数，可能为零
     * @throws IOException
     */
    public int write(ByteBuffer src) throws IOException;

}
