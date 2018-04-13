package ru.track.list;

import java.util.NoSuchElementException;

/**
 * Должен наследовать List
 *
 * Должен иметь 2 конструктора
 * - без аргументов - создает внутренний массив дефолтного размера на ваш выбор
 * - с аргументом - начальный размер массива
 */
public class MyArrayList extends List {

    int[] mass;

    public MyArrayList() {
        mass = new int[10];
        size = 0;
    }

    public MyArrayList(int capacity) {
        mass = new int[capacity];
        size = 0;
    }

    @Override
    void add(int item) {
        if (size == mass.length) {
            int[] newMass = new int[mass.length + 5];
            System.arraycopy(mass, 0, newMass, 0, mass.length);
            mass = newMass;
            mass[size++] = item;
        } else {
            mass[size++] = item;
        }
    }

    @Override
    int remove(int idx) throws NoSuchElementException {
        if (idx > size - 1 || idx < 0) {
            throw new NoSuchElementException();
        }
        if (idx == size - 1) {
            return mass[--size];
        }

        int value = mass[idx];

        for (int i = idx; i < size - 1; i++) {
            mass[i] = mass[i + 1];
        }
        size--;
        return value;
    }

    @Override
    int get(int idx) throws NoSuchElementException {
        if (idx > size - 1 || idx < 0) {
            throw new NoSuchElementException();
        }

        return mass[idx];
    }
}
