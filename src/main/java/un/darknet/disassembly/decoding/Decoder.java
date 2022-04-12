package un.darknet.disassembly.decoding;

import me.martinez.pe.io.CadesBufferStream;
import me.martinez.pe.io.CadesStreamReader;
import me.martinez.pe.io.LittleEndianReader;
import un.darknet.disassembly.Disassembler;
import un.darknet.disassembly.GenericOpcode;
import un.darknet.disassembly.PlatformDisassembler;
import un.darknet.disassembly.exception.InvalidInstructionException;

import java.io.IOException;

public abstract class Decoder {

    public PlatformDisassembler platform;
    protected LittleEndianReader reader;
    protected CadesStreamReader stream; // pointer to reader.getStream();
    protected int length;

    public Decoder(PlatformDisassembler platform) {
        this.platform = platform;
    }

    /**
     * Initialize the decoder.
     */
    public void feed(byte[] data, int offset, int length) {

        stream = new CadesBufferStream(data, offset, length);
        reader = new LittleEndianReader(stream);
        this.length = length;

    }

    /**
     * Advance the reader to the next instruction.
     */
    public DecoderContext next() throws IOException, InvalidInstructionException {

        long pos = stream.getPos(); // save start position for size calculation
        int opcode = reader.readByte();

        DecoderContext ctx = new DecoderContext();
        ctx.opcode = opcode;
        ctx.address = pos;

        try {
            decode(ctx); // send it off to child to decode
        } catch (IOException e) {
            throw new InvalidInstructionException(pos, new GenericOpcode("???", stream.getPos() - pos), e);
        }

        return ctx;
    }

    public boolean hasNext() {
        return stream.getPos() < length;
    }

    /**
     * Decode an instruction based on the DecoderContext.
     *
     * @param ctx the decoder context
     */
    public abstract void decode(DecoderContext ctx) throws IOException;


}
