/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package jdepend.metadata;

import java.io.Serializable;

import org.apache.bcel.Const;

/**
 * Super class for all objects that have modifiers like private, final, ... I.e. classes, fields, and methods.
 *
 * @version $Id: AccessFlags.java 1748636 2016-06-15 20:45:17Z dbrosius $
 */
public abstract class AccessFlags implements Serializable{

    protected int access_flags;

    public AccessFlags() {
    }

    /**
     * @param a
     *            inital access flags
     */
    public AccessFlags(int a) {
        access_flags = a;
    }

    /**
     * @return Access flags of the object aka. "modifiers".
     */
    public int getAccessFlags() {
        return access_flags;
    }

    /**
     * @return Access flags of the object aka. "modifiers".
     */
    public int getModifiers() {
        return access_flags;
    }

    /**
     * Set access flags aka "modifiers".
     *
     * @param access_flags
     *            Access flags of the object.
     */
    public void setAccessFlags(int access_flags) {
        this.access_flags = access_flags;
    }

    /**
     * Set access flags aka "modifiers".
     *
     * @param access_flags
     *            Access flags of the object.
     */
    public void setModifiers(int access_flags) {
        setAccessFlags(access_flags);
    }

    private void setFlag(int flag, boolean set) {
        if ((access_flags & flag) != 0) { // Flag is set already
            if (!set) {
                access_flags ^= flag;
            }
        } else { // Flag not set
            if (set) {
                access_flags |= flag;
            }
        }
    }

    public void isPublic(boolean flag) {
        setFlag(Const.ACC_PUBLIC, flag);
    }

    public boolean isPublic() {
        return (access_flags & Const.ACC_PUBLIC) != 0;
    }

    public void isPrivate(boolean flag) {
        setFlag(Const.ACC_PRIVATE, flag);
    }

    public boolean isPrivate() {
        return (access_flags & Const.ACC_PRIVATE) != 0;
    }

    public void isProtected(boolean flag) {
        setFlag(Const.ACC_PROTECTED, flag);
    }

    public boolean isProtected() {
        return (access_flags & Const.ACC_PROTECTED) != 0;
    }

    public void isStatic(boolean flag) {
        setFlag(Const.ACC_STATIC, flag);
    }

    public boolean isStatic() {
        return (access_flags & Const.ACC_STATIC) != 0;
    }

    public void isFinal(boolean flag) {
        setFlag(Const.ACC_FINAL, flag);
    }

    public boolean isFinal() {
        return (access_flags & Const.ACC_FINAL) != 0;
    }

    public void isSynchronized(boolean flag) {
        setFlag(Const.ACC_SYNCHRONIZED, flag);
    }

    public boolean isSynchronized() {
        return (access_flags & Const.ACC_SYNCHRONIZED) != 0;
    }

    public void isVolatile(boolean flag) {
        setFlag(Const.ACC_VOLATILE, flag);
    }

    public boolean isVolatile() {
        return (access_flags & Const.ACC_VOLATILE) != 0;
    }

    public void isTransient(boolean flag) {
        setFlag(Const.ACC_TRANSIENT, flag);
    }

    public boolean isTransient() {
        return (access_flags & Const.ACC_TRANSIENT) != 0;
    }

    public void isNative(boolean flag) {
        setFlag(Const.ACC_NATIVE, flag);
    }

    public boolean isNative() {
        return (access_flags & Const.ACC_NATIVE) != 0;
    }

    public void isInterface(boolean flag) {
        setFlag(Const.ACC_INTERFACE, flag);
    }

    public boolean isInterface() {
        return (access_flags & Const.ACC_INTERFACE) != 0;
    }

    public void isAbstract(boolean flag) {
        setFlag(Const.ACC_ABSTRACT, flag);
    }

    public boolean isAbstract() {
        return (access_flags & Const.ACC_ABSTRACT) != 0;
    }

    public void isStrictfp(boolean flag) {
        setFlag(Const.ACC_STRICT, flag);
    }

    public boolean isStrictfp() {
        return (access_flags & Const.ACC_STRICT) != 0;
    }

    public void isSynthetic(boolean flag) {
        setFlag(Const.ACC_SYNTHETIC, flag);
    }

    public boolean isSynthetic() {
        return (access_flags & Const.ACC_SYNTHETIC) != 0;
    }

    public void isAnnotation(boolean flag) {
        setFlag(Const.ACC_ANNOTATION, flag);
    }

    public boolean isAnnotation() {
        return (access_flags & Const.ACC_ANNOTATION) != 0;
    }

    public void isEnum(boolean flag) {
        setFlag(Const.ACC_ENUM, flag);
    }

    public boolean isEnum() {
        return (access_flags & Const.ACC_ENUM) != 0;
    }

    public void isVarArgs(boolean flag) {
        setFlag(Const.ACC_VARARGS, flag);
    }

    public boolean isVarArgs() {
        return (access_flags & Const.ACC_VARARGS) != 0;
    }
}
