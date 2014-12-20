package com.algodefu.zlogger.alternative;

import java.util.Map;

import static com.algodefu.zlogger.alternative.DefaultObjectFormatter.DEFAULT_OBJECT_FORMATTER;

/**
 * @author oleg.zherebkin
 */
public final class DefaultObjectFormatterFactory implements ObjectFormatterFactory {

	private final TypeEntityRegistry<ObjectFormatter> formatters =
		new TypeEntityRegistry<ObjectFormatter>( DEFAULT_OBJECT_FORMATTER );

	public <T> void registerObjectFormatter( final Class<T> clazz,
			final ObjectFormatter<T> formatter ) {
		formatters.register( clazz, formatter );
	}

	public void setExtraObjectFormatters( final Map<Class, ObjectFormatter> formatters ) {
		for( final Map.Entry<Class, ObjectFormatter> entry : formatters.entrySet() ) {
			registerObjectFormatter( entry.getKey(), entry.getValue() );
		}
	}

	@Override
	public ObjectFormatter getObjectFormatter( final Object obj ) {
		if( obj == null ) return DEFAULT_OBJECT_FORMATTER;

		final Class type = obj.getClass();
		return formatters.forType( type );
	}
}
