package combinatorics.partition;

import java.util.ArrayList;
import java.util.List;

import combinatorics.CombinatoricsVector;
import combinatorics.Iterator;

/**
 * Iterator for enumeration of all partitions
 */
public class PartitionIterator implements Iterator<CombinatoricsVector<Integer>> {

  /**
   * Generator
   */
  protected final PartitionGenerator     _generator;

  /**
   * Current partition
   */
  protected CombinatoricsVector<Integer> _currentPartition = null;

  /**
   * Current index of partition
   */
  protected long                         _currentIndex     = 0;

  /**
   * Helper vectors
   */
  private int[]                          _mVector          = null;
  private int[]                          _zVector          = null;

  /**
   * Stop criteria
   */
  private int                            _kIndex           = 1;

  /**
   * Constructor
   * 
   * @param generator Generator
   */
  public PartitionIterator(PartitionGenerator generator) {
    _generator = generator;
    _mVector = new int[generator._coreValue + 2];
    _zVector = new int[generator._coreValue + 2];
  }

  /**
   * Initializes the iterator
   */
  @Override
  public void first() {

    if (_generator._coreValue < 1) {
	_kIndex = 0;
	return;
    }

    _currentIndex = 0;
    _kIndex = 1;
    
    setInternalVectorValue(-1, _zVector, 0);
    setInternalVectorValue(-1, _mVector, 0);

    setInternalVectorValue(0, _zVector, _generator._coreValue + 1);
    setInternalVectorValue(0, _mVector, 0);

    setInternalVectorValue(1, _zVector, 1);
    setInternalVectorValue(1, _mVector, _generator._coreValue);
  }

  /**
   * Returns current partition
   */
  @Override
  public CombinatoricsVector<Integer> getCurrentItem() {
    return _currentPartition;
  }

  /**
   * Returns true if all partitions were enumereted
   */
  @Override
  public boolean isDone() {
    return _kIndex == 0;
  }

  /**
   * Moves to the next partition
   */
  @Override
  public void next() {
    _currentIndex++;
    createCurrentPartition(_kIndex);
    int sum = getInternalVectorValue(_kIndex, _mVector) * getInternalVectorValue(_kIndex, _zVector);
    if (getInternalVectorValue(_kIndex, _mVector) == 1) {
      _kIndex--;
      sum += getInternalVectorValue(_kIndex, _mVector) * getInternalVectorValue(_kIndex, _zVector);
    }
    if (getInternalVectorValue(_kIndex - 1, _zVector) == getInternalVectorValue(_kIndex, _zVector) + 1) {
      _kIndex--;
      setInternalVectorValue(_kIndex, _mVector, getInternalVectorValue(_kIndex, _mVector) + 1);
    } else {
      setInternalVectorValue(_kIndex, _zVector, getInternalVectorValue(_kIndex, _zVector) + 1);
      setInternalVectorValue(_kIndex, _mVector, 1);
    }
    if (sum > getInternalVectorValue(_kIndex, _zVector)) {
      setInternalVectorValue(_kIndex + 1, _zVector, 1);
      setInternalVectorValue(_kIndex + 1, _mVector, sum - getInternalVectorValue(_kIndex, _zVector));
      _kIndex++;
    }
  }

  /**
   * Creates current partition based on internal vectors
   */
  private void createCurrentPartition(int k) {
    List<Integer> list = new ArrayList<Integer>();
    for (int index = 1; index <= k; index++) {
      for (int j = 0; j < getInternalVectorValue(index, _mVector); j++) {
        list.add(getInternalVectorValue(index, _zVector));
      }
    }
    _currentPartition = new CombinatoricsVector<Integer>(list);
  }

  private final int getInternalVectorValue(int index, int[] vector) {
    return vector[index + 1];
  }

  private final void setInternalVectorValue(int index, int[] vector, int value) {
    vector[index + 1] = value;
  }

  /**
   * Returns partition as a string
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "PartitionIterator=[#" + _currentIndex + ", " + _currentPartition + "]";
  }
}