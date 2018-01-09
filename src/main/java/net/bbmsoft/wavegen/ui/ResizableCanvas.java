package net.bbmsoft.wavegen.ui;

import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableDoubleProperty;
import javafx.scene.canvas.Canvas;

@SuppressWarnings("all")
public class ResizableCanvas extends Canvas {
	public final static double USE_COMPUTED_SIZE = (-1);

	public final static double USE_PREF_SIZE = Double.NEGATIVE_INFINITY;

	@Override
	public double prefWidth(final double height) {
		final double pref = this.getPrefWidth();
		if ((pref == ResizableCanvas.USE_COMPUTED_SIZE)) {
			return 0.0;
		}
		double _xifexpression = (double) 0;
		if ((Double.isNaN(pref) || (pref < 0))) {
			_xifexpression = 0;
		} else {
			_xifexpression = pref;
		}
		return _xifexpression;
	}

	@Override
	public double prefHeight(final double width) {
		final double pref = this.getPrefHeight();
		if ((pref == ResizableCanvas.USE_COMPUTED_SIZE)) {
			return 0.0;
		}
		double _xifexpression = (double) 0;
		if ((Double.isNaN(pref) || (pref < 0))) {
			_xifexpression = 0;
		} else {
			_xifexpression = pref;
		}
		return _xifexpression;
	}

	@Override
	public double maxWidth(final double height) {
		final double pref = this.getMaxWidth();
		if ((pref == ResizableCanvas.USE_COMPUTED_SIZE)) {
			return super.maxWidth(height);
		}
		if ((pref == ResizableCanvas.USE_PREF_SIZE)) {
			return this.prefWidth(height);
		}
		double _xifexpression = (double) 0;
		if ((Double.isNaN(pref) || (pref < 0))) {
			_xifexpression = 0;
		} else {
			_xifexpression = pref;
		}
		return _xifexpression;
	}

	@Override
	public double maxHeight(final double width) {
		final double pref = this.getMaxHeight();
		if ((pref == ResizableCanvas.USE_COMPUTED_SIZE)) {
			return super.maxHeight(width);
		}
		if ((pref == ResizableCanvas.USE_PREF_SIZE)) {
			return this.prefHeight(width);
		}
		double _xifexpression = (double) 0;
		if ((Double.isNaN(pref) || (pref < 0))) {
			_xifexpression = 0;
		} else {
			_xifexpression = pref;
		}
		return _xifexpression;
	}

	@Override
	public double minWidth(final double height) {
		final double pref = this.getMinWidth();
		if ((pref == ResizableCanvas.USE_COMPUTED_SIZE)) {
			return super.minWidth(height);
		}
		if ((pref == ResizableCanvas.USE_PREF_SIZE)) {
			return this.prefWidth(height);
		}
		double _xifexpression = (double) 0;
		if ((Double.isNaN(pref) || (pref < 0))) {
			_xifexpression = 0;
		} else {
			_xifexpression = pref;
		}
		return _xifexpression;
	}

	@Override
	public double minHeight(final double width) {
		final double pref = this.getMinHeight();
		if ((pref == ResizableCanvas.USE_COMPUTED_SIZE)) {
			return super.minHeight(width);
		}
		if ((pref == ResizableCanvas.USE_PREF_SIZE)) {
			return this.prefHeight(width);
		}
		double _xifexpression = (double) 0;
		if ((Double.isNaN(pref) || (pref < 0))) {
			_xifexpression = 0;
		} else {
			_xifexpression = pref;
		}
		return _xifexpression;
	}

	@Override
	public void resize(final double newWidth, final double newHeight) {
		this.setWidth(newWidth);
		this.setHeight(newHeight);
	}

	private final DoubleProperty _prefWidthProperty = new SimpleDoubleProperty(ResizableCanvas.USE_COMPUTED_SIZE);

	public final double getPrefWidth() {
		return _prefWidthProperty.get();
	}

	public final void setPrefWidth(final double value) {
		_prefWidthProperty.set(value);
	}

	public final DoubleProperty prefWidthProperty() {
		return _prefWidthProperty;
	}

	private final DoubleProperty _prefHeightProperty = new SimpleDoubleProperty(ResizableCanvas.USE_COMPUTED_SIZE);

	public final double getPrefHeight() {
		return _prefHeightProperty.get();
	}

	public final void setPrefHeight(final double value) {
		_prefHeightProperty.set(value);
	}

	public final DoubleProperty prefHeightProperty() {
		return _prefHeightProperty;
	}

	private final DoubleProperty _maxWidthProperty = new SimpleDoubleProperty(Double.MAX_VALUE);

	public final double getMaxWidth() {
		return _maxWidthProperty.get();
	}

	public final void setMaxWidth(final double value) {
		_maxWidthProperty.set(value);
	}

	public final DoubleProperty maxWidthProperty() {
		return _maxWidthProperty;
	}

	private final DoubleProperty _maxHeightProperty = new SimpleDoubleProperty(Double.MAX_VALUE);

	public final double getMaxHeight() {
		return _maxHeightProperty.get();
	}

	public final void setMaxHeight(final double value) {
		_maxHeightProperty.set(value);
	}

	public final DoubleProperty maxHeightProperty() {
		return _maxHeightProperty;
	}

	private final DoubleProperty _minWidthProperty = new SimpleDoubleProperty(ResizableCanvas.USE_COMPUTED_SIZE);

	public final double getMinWidth() {
		return _minWidthProperty.get();
	}

	public final void setMinWidth(final double value) {
		_minWidthProperty.set(value);
	}

	public final DoubleProperty minWidthProperty() {
		return _minWidthProperty;
	}

	private final DoubleProperty _minHeightProperty = new SimpleDoubleProperty(ResizableCanvas.USE_COMPUTED_SIZE);

	public final double getMinHeight() {
		return _minHeightProperty.get();
	}

	public final void setMinHeight(final double value) {
		_minHeightProperty.set(value);
	}

	public final DoubleProperty minHeightProperty() {
		return _minHeightProperty;
	}

	private final BooleanProperty _resizableProperty = new javafx.beans.property.SimpleBooleanProperty(this,
			"resizable", true);

	public final boolean isResizable() {
		return _resizableProperty.get();
	}

	public final void setResizable(final boolean value) {
		_resizableProperty.set(value);
	}

	public final BooleanProperty resizableProperty() {
		return _resizableProperty;
	}

	private static List<CssMetaData<? extends Styleable, ?>> cssMetaDataList;

	public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
		return cssMetaDataList;
	}

	public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
		return getClassCssMetaData();
	}
}
