package com.jgalante.crud.view;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.jgalante.balance.facade.IDAO;
import com.jgalante.balance.view.BaseView;
import com.jgalante.crud.entity.BaseEntity;
import com.jgalante.crud.facade.ICrudController;
import com.jgalante.crud.util.DelegateDataModel;
import com.jgalante.crud.util.ViewState;

public class CrudView<T extends BaseEntity, C extends ICrudController<T, ? extends IDAO>>
		extends BaseView<T, C> {

	private static final long serialVersionUID = 1L;
	private ViewState viewState;
	private DelegateDataModel<T,C> dataModel;

	public CrudView() {
		viewState = ViewState.LIST;
	}
	
	public void edit() {
		setViewState(ViewState.EDIT);
		setEntity((T) getDataModel().getRowData());
	}

	public void remove() {
		try {
			getController().remove(((T) dataModel.getRowData()));
			
//			cleanEntity();

			list();
			
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							getMessage("remove.success"), null));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							getMessage("error"), null));
		}
	}

	public Boolean save() {

		try {
			getController().save(getEntity());

			newEntity();
			
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							getMessage("save.success"), null));
			return true;
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							getMessage("error"), null));
		}

		return false;
	}

	public void cancel() {
		cleanEntity();
		list();
	}

	public void newEntity() {
		viewState = ViewState.NEW;
		setEntity(createEntity());
	}

	public void list() {
		viewState = ViewState.LIST;
	}
	
	public void close() {
		list();
	}
	
	protected void cleanEntity(){
		setEntity(null);
		//TODO: verify first and total page parameters
//		dataModel.setWrappedData(getDataModel().load(0, 10, null, null, null));
//		listaEntidades = null;
	}

	public ViewState getViewState() {
		return viewState;
	}

	public void setViewState(ViewState viewState) {
		this.viewState = viewState;
	}

	public DelegateDataModel<T,C> getDataModel() {
		if (dataModel == null) {
			dataModel = new DelegateDataModel<T, C>((ICrudController<T, ? extends IDAO>) getController());
		}
		
		return dataModel;
	}

	@SuppressWarnings("unchecked")
	@Override
	public C getController() {
		return (C)super.getController();
	}
}
