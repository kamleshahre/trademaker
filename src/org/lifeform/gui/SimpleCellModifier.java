package org.lifeform.gui;

import org.eclipse.jface.viewers.ICellModifier;

public class SimpleCellModifier implements ICellModifier {
	public SimpleCellModifier() {
	}

	public boolean canModify(final Object element, final String property) {
		return true;
	}

	// public Object getValue(Object element, int columnIndex) {
	//
	// Object result = null;
	//
	// switch (columnIndex) {
	// case 0 : // COMPLETED_COLUMN
	// result = new Boolean(task.isCompleted());
	// break;
	// case 1 : // DESCRIPTION_COLUMN
	// result = task.getDescription();
	// break;
	// case 2 : // OWNER_COLUMN
	// String stringValue = task.getOwner();
	// String[] choices = tableViewerExample.getChoices(property);
	// int i = choices.length - 1;
	// while (!stringValue.equals(choices[i]) && i > 0)
	// --i;
	// result = new Integer(i);
	// break;
	// case 3 : // PERCENT_COLUMN
	// result = task.getPercentComplete() + "";
	// break;
	// default :
	// result = "";
	// }
	// return result;
	// }
	//
	// /**
	// * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object,
	// java.lang.String, java.lang.Object)
	// */
	// public void modify(Object element, int columnIndex, Object value) {
	//
	// TableItem item = (TableItem) element;
	// ExampleTask task = (ExampleTask) item.getData();
	// String valueString;
	//
	// switch (columnIndex) {
	// case 0 : // COMPLETED_COLUMN
	// task.setCompleted(((Boolean) value).booleanValue());
	// break;
	// case 1 : // DESCRIPTION_COLUMN
	// valueString = ((String) value).trim();
	// task.setDescription(valueString);
	// break;
	// case 2 : // OWNER_COLUMN
	// valueString = tableViewerExample.getChoices(property)[((Integer)
	// value).intValue()].trim();
	// if (!task.getOwner().equals(valueString)) {
	// task.setOwner(valueString);
	// }
	// break;
	// case 3 : // PERCENT_COLUMN
	// valueString = ((String) value).trim();
	// if (valueString.length() == 0)
	// valueString = "0";
	// task.setPercentComplete(Integer.parseInt(valueString));
	// break;
	// default :
	// }
	// }

	@Override
	public Object getValue(final Object element, final String property) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void modify(final Object element, final String property,
			final Object value) {
		// TODO Auto-generated method stub

	}
}