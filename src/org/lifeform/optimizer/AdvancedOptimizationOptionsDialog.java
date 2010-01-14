package org.lifeform.optimizer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.lifeform.configuration.Defaults;
import org.lifeform.gui.dialog.JBTDialog;
import org.lifeform.service.PreferencesHolder;
import org.lifeform.util.AppUtil;

public class AdvancedOptimizationOptionsDialog extends JBTDialog {
	private static final Dimension FIELD_DIMENSION = new Dimension(
			Integer.MAX_VALUE, 22);
	private final PreferencesHolder prefs;
	private JSlider divideAndConquerCoverageSlider;
	private JTextField strategiesPerProcessorText;

	public AdvancedOptimizationOptionsDialog(final JFrame parent) {
		super(parent);
		prefs = PreferencesHolder.getInstance();
		init();
		pack();
		setLocationRelativeTo(null);
		setModal(true);
		setVisible(true);
	}

	private void add(final JPanel panel, final Defaults pref, final JTextField textField) {
		textField.setText(prefs.get(pref));
		genericAdd(panel, pref, textField, FIELD_DIMENSION);
	}

	private void genericAdd(final JPanel panel, final Defaults pref, final Component comp,
			final Dimension dimension) {
		JLabel fieldNameLabel = new JLabel(pref.getName() + ":");
		fieldNameLabel.setLabelFor(comp);
		comp.setPreferredSize(dimension);
		comp.setMaximumSize(dimension);
		panel.add(fieldNameLabel);
		panel.add(comp);
	}

	private void genericAdd(final JPanel panel, final Defaults pref, final Component comp) {
		genericAdd(panel, pref, comp, null);
	}

	private void add(final JPanel panel, final Defaults pref, final JSlider slider) {
		slider.setValue(prefs.getInt(pref));
		genericAdd(panel, pref, slider);
	}

	private void init() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Advanced Optimization Options");

		JPanel contentPanel = new JPanel(new SpringLayout());

		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5,
				12));
		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("Cancel");
		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		getContentPane().add(buttonsPanel, BorderLayout.SOUTH);

		strategiesPerProcessorText = new JTextField();
		strategiesPerProcessorText.setHorizontalAlignment(JTextField.RIGHT);
		add(contentPanel, Defaults.StrategiesPerProcessor,
				strategiesPerProcessorText);

		int min = 25;
		int max = 1000;
		divideAndConquerCoverageSlider = new JSlider(min, max);
		divideAndConquerCoverageSlider.setMajorTickSpacing(25);
		divideAndConquerCoverageSlider.setPaintTicks(true);
		divideAndConquerCoverageSlider.setSnapToTicks(true);
		Dictionary<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
		Font labelFont = divideAndConquerCoverageSlider.getFont().deriveFont(
				Font.ITALIC, 12);
		JLabel sparserLabel = new JLabel("Sparser");
		sparserLabel.setFont(labelFont);
		JLabel denserLabel = new JLabel("Denser");
		denserLabel.setFont(labelFont);
		labels.put(min, sparserLabel);
		labels.put(max, denserLabel);
		divideAndConquerCoverageSlider.setLabelTable(labels);
		divideAndConquerCoverageSlider.setPaintLabels(true);
		add(contentPanel, Defaults.DivideAndConquerCoverage,
				divideAndConquerCoverageSlider);

		AppUtil.makeCompactGrid(contentPanel, 2, 2, 12, 12, 6, 8);

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				prefs.set(Defaults.DivideAndConquerCoverage,
						divideAndConquerCoverageSlider.getValue());
				prefs.set(Defaults.StrategiesPerProcessor,
						strategiesPerProcessorText.getText());
				dispose();
			}
		});

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				dispose();
			}
		});

		getRootPane().setDefaultButton(okButton);
		setPreferredSize(new Dimension(650, 380));
	}

}
