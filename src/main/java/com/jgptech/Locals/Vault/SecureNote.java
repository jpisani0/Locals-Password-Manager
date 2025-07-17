/*
 * NAME: SecureNote
 * AUTHOR: J. Pisani
 * DATE: 7/15/25
 *
 * DESCRIPTION: Class to hold data for a secure note entry
 */

package com.jgptech.Locals.Vault;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("secureNote")
class SecureNote extends Entry {
    // The SecureNote class is empty as it only contains what the Entry class has: a name, salt, and note.
    // It is still given its own class and file since Entry is meant to be an abstract class that cannot be called
    // directly and for better organization of our data.
}
