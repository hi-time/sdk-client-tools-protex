/*
 * Black Duck Software Suite SDK
 * Copyright (C) 2015  Black Duck Software, Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package com.blackducksoftware.sdk.protex.client.examples;

import java.util.ArrayList;
import java.util.List;

import com.blackducksoftware.sdk.fault.SdkFault;
import com.blackducksoftware.sdk.protex.client.util.ProtexServerProxy;
import com.blackducksoftware.sdk.protex.license.LicenseApi;
import com.blackducksoftware.sdk.protex.obligation.AssignedObligation;

/**
 * This sample demonstrates how to list all obligations for a global license ID.
 * 
 * It demonstrates:
 * - How to get all obligations for a given license ID
 * 
 * See also SampleListLicenseObligations for a variant listing multiple licenses' obligations
 */
public class SampleListLicenseObligationsForLicenseId extends BDProtexSample {

    private static LicenseApi licenseApi = null;

    /**
     * Output usage information for this sample
     */
    private static void usage() {
        String className = SampleListLicenseObligationsForLicenseId.class.getSimpleName();

        List<String> parameters = new ArrayList<String>(getDefaultUsageParameters());
        parameters.add("<license ID>");

        List<String> paramDescriptions = new ArrayList<String>(getDefaultUsageParameterDetails());
        paramDescriptions.add(formatUsageDetail("license ID", "The ID of the license, i.e. \"gpl20\""));

        outputUsageDetails(className, parameters, paramDescriptions);
    }

    public static void main(String[] args) throws Exception {
        // check and save parameters
        if (args.length < 4) {
            System.err.println("Not enough parameters!");
            usage();
            System.exit(-1);
        }

        String serverUri = args[0];
        String username = args[1];
        String password = args[2];
        String licenseId = args[3];

        Long connectionTimeout = 120 * 1000L;

        ProtexServerProxy myProtexServer = null;

        try {
            try {
                myProtexServer = new ProtexServerProxy(serverUri, username, password, connectionTimeout);

                licenseApi = myProtexServer.getLicenseApi();
            } catch (RuntimeException e) {
                System.err.println("\nConnection to server '" + serverUri + "' failed: " + e.getMessage());
                throw e;
            }

            List<AssignedObligation> obligations = null;

            try {
                obligations = licenseApi.getLicenseObligations(licenseId);
            } catch (SdkFault e) {
                System.err.println("getLicenseObligations() failed: " + e.getMessage());
                throw new RuntimeException(e);
            }

            if (obligations == null || obligations.isEmpty()) {
                System.out.println("No Obligations returned");
            } else {
                for (AssignedObligation obligation : obligations) {
                    System.out.println("Obligation: " + obligation.getName() + ", " + obligation.getObligationId());
                }
            }
        } catch (Exception e) {
            System.err.println("SampleListLicenseObligationsForLicenseId failed");
            e.printStackTrace(System.err);
            System.exit(-1);
        } finally {
            // This is optional - it causes the proxy to overwrite the stored password with null characters, increasing
            // security
            if (myProtexServer != null) {
                myProtexServer.close();
            }
        }
    }

}
