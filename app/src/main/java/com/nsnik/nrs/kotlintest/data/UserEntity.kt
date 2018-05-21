/*
 *    Copyright 2018 nsnikhil
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.nsnik.nrs.kotlintest.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.twitter.serial.serializer.ObjectSerializer
import com.twitter.serial.serializer.SerializationContext
import com.twitter.serial.stream.SerializerInput
import com.twitter.serial.stream.SerializerOutput


@Entity
class UserEntity {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    var id: Int = 0

    @SerializedName("name")
    var name: String? = null

    @SerializedName("age")
    var age: Int = 0

    @SerializedName("phone")
    var phone: Double = 0.0

    @SerializedName("address")
    var address: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("date")
    var date: String? = null

    @SerializedName("avatar")
    var avatar: String? = null


    companion object {

        @Ignore
        val SERIALIZER: ObjectSerializer<UserEntity> = UserEntityObjectSerializer()

        class UserEntityObjectSerializer : ObjectSerializer<UserEntity>() {


            override fun serializeObject(context: SerializationContext, output: SerializerOutput<out SerializerOutput<*>>, userEntity: UserEntity) {
                output.writeInt(userEntity.id)
                output.writeString(userEntity.name)
                output.writeInt(userEntity.age)
                output.writeDouble(userEntity.phone)
                output.writeString(userEntity.address)
                output.writeString(userEntity.email)
                output.writeString(userEntity.date)
                output.writeString(userEntity.avatar)
            }

            override fun deserializeObject(context: SerializationContext, input: SerializerInput, versionNumber: Int): UserEntity? {
                val userEntity: UserEntity? = UserEntity()
                userEntity?.id = input.readInt()
                userEntity?.name = input.readString()
                userEntity?.age = input.readInt()
                userEntity?.phone = input.readDouble()
                userEntity?.address = input.readString()
                userEntity?.email = input.readString()
                userEntity?.date = input.readString()
                userEntity?.avatar = input.readString()
                return userEntity
            }

        }
    }

}