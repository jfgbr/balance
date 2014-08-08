package com.jgalante.balance.view;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.jgalante.balance.facade.IController;
import com.jgalante.balance.facade.IDAO;
import com.jgalante.balance.util.DelegateDataModel;
import com.jgalante.balance.util.ViewState;
import com.jgalante.jgcrud.entity.BaseEntity;

public class CrudView<T extends BaseEntity, C extends IController<T, ? extends IDAO>>
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
//		setId(getEntity().getId());
//		setEntity(carregaEntidade());
	}

	public void remove() {
		try {
			getController().remove(((T) dataModel.getRowData()));
//			total--;
			cleanEntity();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							getMessage("remover.sucesso"), null));

			list();
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							getMessage("erro"), null));
		}
	}

	public void save() {

		try {
			getController().save(getEntity());
//			total++;
			cleanEntity();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							getMessage("save.success"), null));

			list();

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							getMessage("save.error"), null));
		}

	}

	public void remover() {

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
//		id = null;
		//TODO: verify first and total page parameters
		dataModel.setWrappedData(getDataModel().load(0, 10, null, null, null));
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
			dataModel = new DelegateDataModel<T, C>(getController());
		}
		
		return dataModel;
	}

}
