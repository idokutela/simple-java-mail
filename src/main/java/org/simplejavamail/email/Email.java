package org.simplejavamail.email;

import javax.activation.DataSource;
import javax.annotation.Nonnull;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;
import static org.simplejavamail.internal.util.MiscUtil.valueNullOrEmpty;
import static org.simplejavamail.internal.util.Preconditions.checkNonEmptyArgument;

/**
 * Email message with all necessary data for an effective mailing action, including attachments etc. Exclusively created using {@link EmailBuilder}.
 */
@SuppressWarnings("SameParameterValue")
public class Email {
	
	/**
	 * @see EmailPopulatingBuilder#fixingMessageId(String)
	 */
	private String id;
	
	/**
	 * @see EmailPopulatingBuilder#from(Recipient)
	 */
	private final Recipient fromRecipient;
	
	/**
	 * @see EmailPopulatingBuilder#withReplyTo(Recipient)
	 */
	private final Recipient replyToRecipient;
	
	/**
	 * @see EmailPopulatingBuilder#withBounceTo(Recipient)
	 */
	private final Recipient bounceToRecipient;
	
	/**
	 * @see EmailPopulatingBuilder#withPlainText(String)
	 */
	private final String text;
	
	/**
	 * @see EmailPopulatingBuilder#withHTMLText(String)
	 */
	private final String textHTML;
	
	/**
	 * @see EmailPopulatingBuilder#withCalendarText(CalendarMethod, String)
	 */
	private final CalendarMethod calendarMethod;
	
	/**
	 * @see EmailPopulatingBuilder#withCalendarText(CalendarMethod, String)
	 */
	private final String textCalendar;
	
	/**
	 * @see EmailPopulatingBuilder#withSubject(String)
	 */
	private final String subject;
	
	/**
	 * @see EmailPopulatingBuilder#to(Recipient...)
	 * @see EmailPopulatingBuilder#cc(Recipient...)
	 * @see EmailPopulatingBuilder#bcc(Recipient...)
	 */
	private final List<Recipient> recipients;
	
	/**
	 * @see EmailPopulatingBuilder#withEmbeddedImage(String, DataSource)
	 */
	private final List<AttachmentResource> embeddedImages;
	
	/**
	 * @see EmailPopulatingBuilder#withAttachment(String, DataSource)
	 */
	private final List<AttachmentResource> attachments;
	
	/**
	 * @see EmailPopulatingBuilder#withHeader(String, Object)
	 * @see EmailBuilder#replyingTo(MimeMessage, boolean, String)
	 */
	private final Map<String, String> headers;
	
	/**
	 * @see EmailPopulatingBuilder#withDispositionNotificationTo()
	 * @see EmailPopulatingBuilder#withDispositionNotificationTo(Recipient)
	 */
	private final boolean useDispositionNotificationTo;
	
	/**
	 * @see EmailPopulatingBuilder#withDispositionNotificationTo()
	 * @see EmailPopulatingBuilder#withDispositionNotificationTo(Recipient)
	 */
	private Recipient dispositionNotificationTo;
	
	/**
	 * @see EmailPopulatingBuilder#withReturnReceiptTo()
	 * @see EmailPopulatingBuilder#withReturnReceiptTo(Recipient)
	 */
	private final boolean useReturnReceiptTo;
	
	/**
	 * @see EmailPopulatingBuilder#withReturnReceiptTo()
	 * @see EmailPopulatingBuilder#withReturnReceiptTo(Recipient)
	 */
	private Recipient returnReceiptTo;
	
	/**
	 * @see EmailBuilder#forwarding(MimeMessage)
	 */
	private final MimeMessage emailToForward;
	
	/**
	 * Simply transfers everything from {@link EmailPopulatingBuilder} to this Email instance.
	 *
	 * @see EmailPopulatingBuilder#buildEmail()
	 */
	public Email(@Nonnull final EmailPopulatingBuilder builder) {
		checkNonEmptyArgument(builder, "builder");
		recipients = unmodifiableList(builder.getRecipients());
		embeddedImages = unmodifiableList(builder.getEmbeddedImages());
		attachments = unmodifiableList(builder.getAttachments());
		headers = unmodifiableMap(builder.getHeaders());
		
		id = builder.getId();
		fromRecipient = builder.getFromRecipient();
		replyToRecipient = builder.getReplyToRecipient();
		bounceToRecipient = builder.getBounceToRecipient();
		text = builder.getText();
		textHTML = builder.getTextHTML();
		calendarMethod = builder.getCalendarMethod();
		textCalendar = builder.getTextCalendar();
		subject = builder.getSubject();
		
		useDispositionNotificationTo = builder.isUseDispositionNotificationTo();
		useReturnReceiptTo = builder.isUseReturnReceiptTo();
		dispositionNotificationTo = builder.getDispositionNotificationTo();
		returnReceiptTo = builder.getReturnReceiptTo();
		emailToForward = builder.getEmailToForward();
		
		if (useDispositionNotificationTo && valueNullOrEmpty(builder.getDispositionNotificationTo())) {
			//noinspection IfMayBeConditional
			if (builder.getReplyToRecipient() != null) {
				dispositionNotificationTo = builder.getReplyToRecipient();
			} else {
				dispositionNotificationTo = builder.getFromRecipient();
			}
		}
		
		if (useReturnReceiptTo && valueNullOrEmpty(builder.getDispositionNotificationTo())) {
			//noinspection IfMayBeConditional
			if (builder.getReplyToRecipient() != null) {
				returnReceiptTo = builder.getReplyToRecipient();
			} else {
				returnReceiptTo = builder.getFromRecipient();
			}
		}
		
	}
	
	/**
	 * @deprecated Don't use this method, refer to EmailPopulatingBuilder#fixingMessageId(String) instead. This method is used internally to update
	 * the message id once a mail has been sent.
	 */
	@Deprecated
	@SuppressWarnings("DeprecatedIsStillUsed")
	public void internalSetId(@Nonnull final String id) {
		this.id = id;
	}
	
	@SuppressWarnings("SameReturnValue")
	@Override
	public int hashCode() {
		return 0;
	}
	
	@Override
	public boolean equals(final Object o) {
		return (this == o) || ((o != null) && (getClass() == o.getClass()) &&
				EqualsHelper.equalsEmail(this, (Email) o));
	}
	
	@Override
	public String toString() {
		String s = "Email{" +
				"\n\tid=" + id +
				"\n\tfromRecipient=" + fromRecipient +
				",\n\treplyToRecipient=" + replyToRecipient +
				",\n\tbounceToRecipient=" + bounceToRecipient +
				",\n\ttext='" + text + '\'' +
				",\n\ttextHTML='" + textHTML + '\'' +
				",\n\ttextCalendar='" + textCalendar + '\'' +
				",\n\tsubject='" + subject + '\'' +
				",\n\trecipients=" + recipients;

		if (useDispositionNotificationTo) {
			s += ",\n\tuseDispositionNotificationTo=" + true +
					",\n\t\tdispositionNotificationTo=" + dispositionNotificationTo;
		}
		if (useReturnReceiptTo) {
			s += ",\n\tuseReturnReceiptTo=" + true +
					",\n\t\treturnReceiptTo=" + returnReceiptTo;
		}
		if (!headers.isEmpty()) {
			s += ",\n\theaders=" + headers;
		}
		if (!embeddedImages.isEmpty()) {
			s += ",\n\tembeddedImages=" + embeddedImages;
		}
		if (!attachments.isEmpty()) {
			s += ",\n\tattachments=" + attachments;
		}
		if (emailToForward != null) {
			s += ",\n\tforwardingEmail=true";
		}
		s += "\n}";
		return s;
	}
	
	/**
	 * @see EmailPopulatingBuilder#fixingMessageId(String)
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @see EmailPopulatingBuilder#from(Recipient)
	 */
	public Recipient getFromRecipient() {
		return fromRecipient;
	}
	
	/**
	 * @see EmailPopulatingBuilder#withReplyTo(Recipient)
	 */
	public Recipient getReplyToRecipient() {
		return replyToRecipient;
	}
	
	/**
	 * @see EmailPopulatingBuilder#withBounceTo(Recipient)
	 */
	public Recipient getBounceToRecipient() {
		return bounceToRecipient;
	}
	
	/**
	 * @see EmailPopulatingBuilder#withSubject(String)
	 */
	public String getSubject() {
		return subject;
	}
	
	/**
	 * @see EmailPopulatingBuilder#withDispositionNotificationTo()
	 * @see EmailPopulatingBuilder#withDispositionNotificationTo(Recipient)
	 */
	public boolean isUseDispositionNotificationTo() {
		return useDispositionNotificationTo;
	}
	
	/**
	 * @see EmailPopulatingBuilder#withDispositionNotificationTo()
	 * @see EmailPopulatingBuilder#withDispositionNotificationTo(Recipient)
	 */
	public Recipient getDispositionNotificationTo() {
		return dispositionNotificationTo;
	}
	
	/**
	 * @see EmailPopulatingBuilder#withReturnReceiptTo()
	 * @see EmailPopulatingBuilder#withReturnReceiptTo(Recipient)
	 */
	public boolean isUseReturnReceiptTo() {
		return useReturnReceiptTo;
	}
	
	/**
	 * @see EmailPopulatingBuilder#withReturnReceiptTo()
	 * @see EmailPopulatingBuilder#withReturnReceiptTo(Recipient)
	 */
	public Recipient getReturnReceiptTo() {
		return returnReceiptTo;
	}
	
	/**
	 * @see EmailBuilder#forwarding(MimeMessage)
	 */
	public MimeMessage getEmailToForward() {
		return emailToForward;
	}
	
	/**
	 * @see EmailPopulatingBuilder#withPlainText(String)
	 */
	public String getPlainText() {
		return text;
	}
	
	/**
	 * @see EmailPopulatingBuilder#withHTMLText(String)
	 */
	public String getHTMLText() {
		return textHTML;
	}

	/**
	 * @see EmailPopulatingBuilder#withCalendarText(CalendarMethod, String)
	 */
	public CalendarMethod getCalendarMethod() {
		return calendarMethod;
	}

	/**
	 * @see EmailPopulatingBuilder#withCalendarText(CalendarMethod, String)
	 */
	public String getCalendarText() {
		return textCalendar;
	}

	/**
	 * @see EmailPopulatingBuilder#withAttachment(String, DataSource)
	 */
	public List<AttachmentResource> getAttachments() {
		return attachments;
	}
	
	/**
	 * @see EmailPopulatingBuilder#withEmbeddedImage(String, DataSource)
	 */
	public List<AttachmentResource> getEmbeddedImages() {
		return embeddedImages;
	}
	
	/**
	 * @see EmailPopulatingBuilder#to(Recipient...)
	 * @see EmailPopulatingBuilder#cc(Recipient...)
	 * @see EmailPopulatingBuilder#bcc(Recipient...)
	 */
	public List<Recipient> getRecipients() {
		return recipients;
	}
	
	/**
	 * @see EmailPopulatingBuilder#withHeader(String, Object)
	 * @see EmailBuilder#replyingTo(MimeMessage, boolean, String)
	 */
	public Map<String, String> getHeaders() {
		return headers;
	}
	
}
