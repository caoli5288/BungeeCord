package net.md_5.bungee.compress;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.Setter;
import net.md_5.bungee.jni.zlib.BungeeZlib;
import net.md_5.bungee.jni.zlib.JavaZlib;
import net.md_5.bungee.protocol.DefinedPacket;

public class PacketCompressor extends MessageToByteEncoder<ByteBuf>
{

    private final BungeeZlib zlib = new JavaZlib();
    @Setter
    private int threshold = 256;
    private int level;

    public PacketCompressor(int level) {
        this.level = level;
    }

    public PacketCompressor() {
        this(3);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception
    {
        zlib.init( true, level);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception
    {
        zlib.free();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception
    {
        int origSize = msg.readableBytes();
        if ( origSize < threshold )
        {
            DefinedPacket.writeVarInt( 0, out );
            out.writeBytes( msg );
        } else
        {
            DefinedPacket.writeVarInt( origSize, out );

            zlib.process( msg, out );
        }
    }
}
