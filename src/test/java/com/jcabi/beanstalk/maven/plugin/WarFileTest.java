/**
 * Copyright (c) 2012-2014, jcabi.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met: 1) Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the following
 * disclaimer. 2) Redistributions in binary form must reproduce the above
 * copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided
 * with the distribution. 3) Neither the name of the jcabi.com nor
 * the names of its contributors may be used to endorse or promote
 * products derived from this software without specific prior written
 * permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.jcabi.beanstalk.maven.plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import org.apache.maven.plugin.MojoFailureException;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * Unit tests for the WarFile class.
 * @author Dmitri Pisarenko (dp@altruix.co)
 * @version $Id$
 * @since 1.0
 * @checkstyle MultipleStringLiteralsCheck (500 lines)
 */
public final class WarFileTest {
    /**
     * Verifies that checkEbextensionsValidity throws an exception, if there is
     * no .ebextensions directory in the WAR file.
     * @throws java.io.IOException Thrown in case of error.
     */
    @Test
    public void checkEbextensionsValidityThrowsExceptionNoDir()
        throws IOException {
        final ZipFile zip = Mockito.mock(ZipFile.class);
        Mockito.when(zip.getEntry(".ebextensions")).thenReturn(null);
        final WarFile war = new WarFile(zip);
        try {
            war.checkEbextensionsValidity();
        } catch (final MojoFailureException exception) {
            MatcherAssert.assertThat(
                exception.getMessage(),
                Matchers.equalTo(
                    ".ebextensions directory does not exist in the WAR file"
                )
            );
        }
    }
    /**
     * Verifies that checkEbextensionsValidity throws an exception, if the
     * .ebextensions is empty.
     * @throws IOException Thrown in case of error.
     */
    @Test
    @SuppressWarnings("unchecked")
    public void checkEbextensionsValidityThrowsExceptionNoConfigFiles()
        throws IOException {
        final File temp = File.createTempFile("test", ".zip");
        final FileOutputStream fos = new FileOutputStream(temp);
        final ZipOutputStream out = new ZipOutputStream(fos);
        out.putNextEntry(new ZipEntry(".ebextensions/"));
        out.flush();
        out.close();
        fos.flush();
        fos.close();
        final WarFile war = new WarFile(new ZipFile(temp));
        try {
            war.checkEbextensionsValidity();
        } catch (final MojoFailureException exception) {
            MatcherAssert.assertThat(
                exception.getMessage(),
                Matchers.equalTo(
                    ".ebextensions contains no config files."
                )
            );
        }
    }
}
