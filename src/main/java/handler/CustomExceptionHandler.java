package handler;

import exception.AppException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExceptionHandler;
import jakarta.faces.context.ExceptionHandlerWrapper;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.Flash;
import jakarta.faces.event.ExceptionQueuedEvent;

import java.util.Iterator;

@ApplicationScoped
public class CustomExceptionHandler extends ExceptionHandlerWrapper {

	private final ExceptionHandler wrapped;

	public CustomExceptionHandler(ExceptionHandler wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public ExceptionHandler getWrapped() {
		return wrapped;
	}

	@Override
	public void handle() {
		Iterator<ExceptionQueuedEvent> events = getUnhandledExceptionQueuedEvents().iterator();

		while (events.hasNext()) {
			ExceptionQueuedEvent event = events.next();
			Throwable t = event.getContext().getException();

			try {
				if (t instanceof AppException appEx) {
					FacesContext fc = FacesContext.getCurrentInstance();
					fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, appEx.getMessage(), null));

					Flash flash = fc.getExternalContext().getFlash();
					flash.setKeepMessages(true);
					fc.getApplication().getNavigationHandler().handleNavigation(fc, null,
							"/error.xhtml?faces-redirect=true");
					fc.renderResponse();
				}
			} finally {
				events.remove();
			}
		}

		getWrapped().handle();
	}
}
