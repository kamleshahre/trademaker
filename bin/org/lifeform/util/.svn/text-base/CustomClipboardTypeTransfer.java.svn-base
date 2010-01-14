package org.lifeform.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TransferData;

public class CustomClipboardTypeTransfer extends ByteArrayTransfer {

	private static final String CustomClipboardTypeNAME = "name_list"; //$NON-NLS-1$
	private static final int CustomClipboardTypeID = registerType(CustomClipboardTypeNAME);
	private static CustomClipboardTypeTransfer _instance = new CustomClipboardTypeTransfer();

	public static CustomClipboardTypeTransfer getInstance() {
		return _instance;
	}

	@Override
	public void javaToNative(final Object object, final TransferData transferData) {
		if (!checkCustomClipboardType(object) || !isSupportedType(transferData)) {
			DND.error(DND.ERROR_INVALID_DATA);
		}
		CustomClipboardType[] CustomClipboardTypes = (CustomClipboardType[]) object;
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			DataOutputStream writeOut = new DataOutputStream(out);
			for (int i = 0, length = CustomClipboardTypes.length; i < length; i++) {
				byte[] buffer = CustomClipboardTypes[i].getData();
				writeOut.writeInt(buffer.length);
				writeOut.write(buffer);
			}
			byte[] buffer = out.toByteArray();
			writeOut.close();
			super.javaToNative(buffer, transferData);
		} catch (IOException e) {
		}
	}

	@Override
	public Object nativeToJava(final TransferData transferData) {
		if (isSupportedType(transferData)) {

			byte[] buffer = (byte[]) super.nativeToJava(transferData);
			if (buffer == null)
				return null;

			CustomClipboardType[] myData = new CustomClipboardType[0];
			try {
				ByteArrayInputStream in = new ByteArrayInputStream(buffer);
				DataInputStream readIn = new DataInputStream(in);
				while (readIn.available() > 20) {
					int size = readIn.readInt();
					byte[] name = new byte[size];
					readIn.read(name);
					CustomClipboardType datum = CustomClipboardType
							.fromData(name);
					CustomClipboardType[] newMyData = new CustomClipboardType[myData.length + 1];
					System.arraycopy(myData, 0, newMyData, 0, myData.length);
					newMyData[myData.length] = datum;
					myData = newMyData;
				}
				readIn.close();
			} catch (IOException ex) {
				return null;
			}
			return myData;
		}

		return null;
	}

	@Override
	protected String[] getTypeNames() {
		return new String[] { CustomClipboardTypeNAME };
	}

	@Override
	protected int[] getTypeIds() {
		return new int[] { CustomClipboardTypeID };
	}

	boolean checkCustomClipboardType(final Object object) {
		// if (object == null || !(object instanceof CustomClipboardType[])
		// || ((CustomClipboardType[]) object).length == 0)
		// return false;
		// CustomClipboardType[] CustomClipboardTypes = (CustomClipboardType[])
		// object;
		// for (int i = 0; i < CustomClipboardTypes.length; i++) {
		// if (CustomClipboardTypes[i] == null
		// || CustomClipboardTypes[i].firstName == null
		// || CustomClipboardTypes[i].firstName.length() == 0
		// || CustomClipboardTypes[i].lastName == null
		// || CustomClipboardTypes[i].lastName.length() == 0)
		// return false;
		// }
		return true;
	}

	@Override
	protected boolean validate(final Object object) {
		return checkCustomClipboardType(object);
	}
}
