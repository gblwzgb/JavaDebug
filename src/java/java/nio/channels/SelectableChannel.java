/*
 * Copyright (c) 2000, 2013, Oracle and/or its affiliates. All rights reserved.
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
import java.nio.channels.spi.AbstractInterruptibleChannel;
import java.nio.channels.spi.SelectorProvider;


/**
 * A channel that can be multiplexed via a {@link Selector}.
 *
 * <p> In order to be used with a selector, an instance of this class must
 * first be <i>registered</i> via the {@link #register(Selector,int,Object)
 * register} method.  This method returns a new {@link SelectionKey} object
 * that represents the channel's registration with the selector.
 *
 * <p> Once registered with a selector, a channel remains registered until it
 * is <i>deregistered</i>.  This involves deallocating whatever resources were
 * allocated to the channel by the selector.
 *
 * <p> A channel cannot be deregistered directly; instead, the key representing
 * its registration must be <i>cancelled</i>.  Cancelling a key requests that
 * the channel be deregistered during the selector's next selection operation.
 * A key may be cancelled explicitly by invoking its {@link
 * SelectionKey#cancel() cancel} method.  All of a channel's keys are cancelled
 * implicitly when the channel is closed, whether by invoking its {@link
 * Channel#close close} method or by interrupting a thread blocked in an I/O
 * operation upon the channel.
 *
 * <p> If the selector itself is closed then the channel will be deregistered,
 * and the key representing its registration will be invalidated, without
 * further delay.
 *
 * <p> A channel may be registered at most once with any particular selector.
 *
 * <p> Whether or not a channel is registered with one or more selectors may be
 * determined by invoking the {@link #isRegistered isRegistered} method.
 *
 * <p> Selectable channels are safe for use by multiple concurrent
 * threads. </p>
 *
 *
 * <a name="bm"></a>
 * <h2>Blocking mode</h2>
 *
 * A selectable channel is either in <i>blocking</i> mode or in
 * <i>non-blocking</i> mode.  In blocking mode, every I/O operation invoked
 * upon the channel will block until it completes.  In non-blocking mode an I/O
 * operation will never block and may transfer fewer bytes than were requested
 * or possibly no bytes at all.  The blocking mode of a selectable channel may
 * be determined by invoking its {@link #isBlocking isBlocking} method.
 *
 * <p> Newly-created selectable channels are always in blocking mode.
 * Non-blocking mode is most useful in conjunction with selector-based
 * multiplexing.  A channel must be placed into non-blocking mode before being
 * registered with a selector, and may not be returned to blocking mode until
 * it has been deregistered.
 *
 *
 * @author Mark Reinhold
 * @author JSR-51 Expert Group
 * @since 1.4
 *
 * @see SelectionKey
 * @see Selector
 */

/**
 * 可以通过Selector多路复用的通道。
 *
 * 为了与selector一起使用，必须首先通过register方法注册此类的实例。
 * 此方法返回一个新的SelectionKey对象，该对象表示通道在selector中的注册。
 *
 * 向选择器注册后，通道将保持注册状态，直到注销为止。
 * 这涉及取消分配由selector分配给该通道的任何资源。
 *
 * 通道不能直接注销。相反，代表其注册的key必须被取消。
 * 取消key要求在selector的下一个选择操作期间注销通道。
 * 可以通过调用其cancel方法显式地取消key。
 * 无论是调用通道的close方法还是通过中断通道的I/O操作中阻塞的线程来关闭通道，都将隐式取消通道的所有keys。
 * 如果selector本身已关闭，则该通道将被注销，并且表示其注册的key将无效，而不会造成进一步延迟。
 *
 * 一个频道最多可以使用任何一个特定的selector注册一次。
 *
 * 可以通过调用isRegistered方法来确定是否向一个或多个选择器注册了通道。
 *
 * Selectable channels可以安全地供多个并发线程使用。
 *
 * 阻塞模式
 *
 * Selectable channels处于阻塞模式或非阻塞模式。
 * 在阻塞模式下，在通道上调用的每个I/O操作都将阻塞，直到完成为止。
 * 在非阻塞模式下，I/O操作将永远不会阻塞，并且可能传输的字节数少于请求的字节数，或者根本没有字节传输。
 * Selectable channels的阻塞模式可以通过调用其isBlocking方法来确定。
 *
 * 新创建的Selectable channels始终处于阻塞模式。
 * 非阻塞模式与基于selector的多路复用一起使用最有用。
 * 在使用selector注册之前，必须将通道置于非阻塞模式，并且在注销之前，不得将其返回到阻塞模式。
 *
 */
public abstract class SelectableChannel
    extends AbstractInterruptibleChannel
    implements Channel
{

    /**
     * Initializes a new instance of this class.
     */
    protected SelectableChannel() { }

    /**
     * Returns the provider that created this channel.
     *
     * @return  The provider that created this channel
     */
    public abstract SelectorProvider provider();

    /**
     * Returns an <a href="SelectionKey.html#opsets">operation set</a>
     * identifying this channel's supported operations.  The bits that are set
     * in this integer value denote exactly the operations that are valid for
     * this channel.  This method always returns the same value for a given
     * concrete channel class.
     *
     * @return  The valid-operation set
     */
    public abstract int validOps();

    // Internal state:
    //   keySet, may be empty but is never null, typ. a tiny array
    //   boolean isRegistered, protected by key set
    //   regLock, lock object to prevent duplicate registrations
    //   boolean isBlocking, protected by regLock

    /**
     * Tells whether or not this channel is currently registered with any
     * selectors.  A newly-created channel is not registered.
     *
     * <p> Due to the inherent delay between key cancellation and channel
     * deregistration, a channel may remain registered for some time after all
     * of its keys have been cancelled.  A channel may also remain registered
     * for some time after it is closed.  </p>
     *
     * @return <tt>true</tt> if, and only if, this channel is registered
     */
    public abstract boolean isRegistered();
    //
    // sync(keySet) { return isRegistered; }

    /**
     * Retrieves the key representing the channel's registration with the given
     * selector.
     *
     * @param   sel
     *          The selector
     *
     * @return  The key returned when this channel was last registered with the
     *          given selector, or <tt>null</tt> if this channel is not
     *          currently registered with that selector
     */
    public abstract SelectionKey keyFor(Selector sel);
    //
    // sync(keySet) { return findKey(sel); }

    /**
     * Registers this channel with the given selector, returning a selection
     * key.
     *
     * <p> If this channel is currently registered with the given selector then
     * the selection key representing that registration is returned.  The key's
     * interest set will have been changed to <tt>ops</tt>, as if by invoking
     * the {@link SelectionKey#interestOps(int) interestOps(int)} method.  If
     * the <tt>att</tt> argument is not <tt>null</tt> then the key's attachment
     * will have been set to that value.  A {@link CancelledKeyException} will
     * be thrown if the key has already been cancelled.
     *
     * <p> Otherwise this channel has not yet been registered with the given
     * selector, so it is registered and the resulting new key is returned.
     * The key's initial interest set will be <tt>ops</tt> and its attachment
     * will be <tt>att</tt>.
     *
     * <p> This method may be invoked at any time.  If this method is invoked
     * while another invocation of this method or of the {@link
     * #configureBlocking(boolean) configureBlocking} method is in progress
     * then it will first block until the other operation is complete.  This
     * method will then synchronize on the selector's key set and therefore may
     * block if invoked concurrently with another registration or selection
     * operation involving the same selector. </p>
     *
     * <p> If this channel is closed while this operation is in progress then
     * the key returned by this method will have been cancelled and will
     * therefore be invalid. </p>
     *
     * @param  sel
     *         The selector with which this channel is to be registered
     *
     * @param  ops
     *         The interest set for the resulting key
     *
     * @param  att
     *         The attachment for the resulting key; may be <tt>null</tt>
     *
     * @throws  ClosedChannelException
     *          If this channel is closed
     *
     * @throws  ClosedSelectorException
     *          If the selector is closed
     *
     * @throws  IllegalBlockingModeException
     *          If this channel is in blocking mode
     *
     * @throws  IllegalSelectorException
     *          If this channel was not created by the same provider
     *          as the given selector
     *
     * @throws  CancelledKeyException
     *          If this channel is currently registered with the given selector
     *          but the corresponding key has already been cancelled
     *
     * @throws  IllegalArgumentException
     *          If a bit in the <tt>ops</tt> set does not correspond to an
     *          operation that is supported by this channel, that is, if
     *          {@code set & ~validOps() != 0}
     *
     * @return  A key representing the registration of this channel with the given selector
     */
    /**
     * 使用给定的selector注册此channel，并返回selection key。
     *
     * 如果此channel当前已在给定的selector中注册，则返回代表该注册的selection key。
     * 该key的interest set将更改为ops，就像通过调用interestOps(int)方法一样。
     * 如果att参数不为null，则key的attachment将被设置为该值。
     * 如果key已被取消，则将引发CancelledKeyException。
     *
     * 否则，此channel尚未在给定的selector中注册，所以已注册并返回生成的新key。
     * key的初始interest set为ops，其attachment为att。
     *
     * 可以随时调用此方法。
     * 如果在此方法或configureBlocking方法的另一次调用正在进行时调用此方法，则它将首先阻塞，直到完成其他操作为止。
     * 然后，此方法将在selector的key set上进行同步，因此如果与涉及同一selector的另一个注册或选择操作同时调用，则可能会阻塞。
     *
     * 如果在执行此操作时关闭了此通道，则此方法返回的key将被取消，因此将无效。
     *
     * @param sel 要注册此通道的selector
     * @param ops 返回的key，所关心的interest set
     * @param att 返回的key关联的attachment，可能为null
     * @return 代表此channel在给定selector中的注册的key
     * @throws ClosedChannelException
     */
    public abstract SelectionKey register(Selector sel, int ops, Object att)
        throws ClosedChannelException;
    //
    // sync(regLock) {
    //   sync(keySet) { look for selector }
    //   if (channel found) { set interest ops -- may block in selector;
    //                        return key; }
    //   create new key -- may block somewhere in selector;
    //   sync(keySet) { add key; }
    //   attach(attachment);
    //   return key;
    // }

    /**
     * Registers this channel with the given selector, returning a selection
     * key.
     *
     * <p> An invocation of this convenience method of the form
     *
     * <blockquote><tt>sc.register(sel, ops)</tt></blockquote>
     *
     * behaves in exactly the same way as the invocation
     *
     * <blockquote><tt>sc.{@link
     * #register(java.nio.channels.Selector,int,java.lang.Object)
     * register}(sel, ops, null)</tt></blockquote>
     *
     * @param  sel
     *         The selector with which this channel is to be registered
     *
     * @param  ops
     *         The interest set for the resulting key
     *
     * @throws  ClosedChannelException
     *          If this channel is closed
     *
     * @throws  ClosedSelectorException
     *          If the selector is closed
     *
     * @throws  IllegalBlockingModeException
     *          If this channel is in blocking mode
     *
     * @throws  IllegalSelectorException
     *          If this channel was not created by the same provider
     *          as the given selector
     *
     * @throws  CancelledKeyException
     *          If this channel is currently registered with the given selector
     *          but the corresponding key has already been cancelled
     *
     * @throws  IllegalArgumentException
     *          If a bit in <tt>ops</tt> does not correspond to an operation
     *          that is supported by this channel, that is, if {@code set &
     *          ~validOps() != 0}
     *
     * @return  A key representing the registration of this channel with
     *          the given selector
     */
    public final SelectionKey register(Selector sel, int ops)
        throws ClosedChannelException
    {
        return register(sel, ops, null);
    }

    /**
     * Adjusts this channel's blocking mode.
     *
     * <p> If this channel is registered with one or more selectors then an
     * attempt to place it into blocking mode will cause an {@link
     * IllegalBlockingModeException} to be thrown.
     *
     * <p> This method may be invoked at any time.  The new blocking mode will
     * only affect I/O operations that are initiated after this method returns.
     * For some implementations this may require blocking until all pending I/O
     * operations are complete.
     *
     * <p> If this method is invoked while another invocation of this method or
     * of the {@link #register(Selector, int) register} method is in progress
     * then it will first block until the other operation is complete. </p>
     *
     * @param  block  If <tt>true</tt> then this channel will be placed in
     *                blocking mode; if <tt>false</tt> then it will be placed
     *                non-blocking mode
     *
     * @return  This selectable channel
     *
     * @throws  ClosedChannelException
     *          If this channel is closed
     *
     * @throws  IllegalBlockingModeException
     *          If <tt>block</tt> is <tt>true</tt> and this channel is
     *          registered with one or more selectors
     *
     * @throws IOException
     *         If an I/O error occurs
     */
    public abstract SelectableChannel configureBlocking(boolean block)
        throws IOException;
    //
    // sync(regLock) {
    //   sync(keySet) { throw IBME if block && isRegistered; }
    //   change mode;
    // }

    /**
     * Tells whether or not every I/O operation on this channel will block
     * until it completes.  A newly-created channel is always in blocking mode.
     *
     * <p> If this channel is closed then the value returned by this method is
     * not specified. </p>
     *
     * @return <tt>true</tt> if, and only if, this channel is in blocking mode
     */
    public abstract boolean isBlocking();

    /**
     * Retrieves the object upon which the {@link #configureBlocking
     * configureBlocking} and {@link #register register} methods synchronize.
     * This is often useful in the implementation of adaptors that require a
     * specific blocking mode to be maintained for a short period of time.
     *
     * @return  The blocking-mode lock object
     */
    public abstract Object blockingLock();

}
