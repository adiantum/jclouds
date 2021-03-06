package org.jclouds.rackspace.cloudservers.compute;

import static org.jclouds.compute.util.ComputeServiceUtils.getCores;
import static org.testng.Assert.assertEquals;

import java.util.Set;

import org.jclouds.compute.BaseTemplateBuilderLiveTest;
import org.jclouds.compute.domain.OsFamily;
import org.jclouds.compute.domain.OsFamilyVersion64Bit;
import org.jclouds.compute.domain.Template;
import org.testng.annotations.Test;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;

/**
 * 
 * @author Adrian Cole
 */
@Test(groups = "live")
public class CloudServersUSTemplateBuilderLiveTest extends BaseTemplateBuilderLiveTest {

   public CloudServersUSTemplateBuilderLiveTest() {
      provider = "cloudservers-us";
   }

   @Override
   protected Predicate<OsFamilyVersion64Bit> defineUnsupportedOperatingSystems() {
      return new Predicate<OsFamilyVersion64Bit>() {

         @Override
         public boolean apply(OsFamilyVersion64Bit input) {
            switch (input.family) {
               case UBUNTU:
                  return input.version.equals("11.04") || input.version.equals("8.04") || !input.is64Bit;
               case CENTOS:
                  return input.version.matches("5.[023]") || !input.is64Bit;
               case WINDOWS:
                  return input.version.equals("2008") || input.version.indexOf("2003") != -1
                           || (input.version.equals("2008 R2") && !input.is64Bit);
               default:
                  return true;
            }
         }

      };
   }

   @Test
   public void testTemplateBuilder() {
      Template defaultTemplate = this.context.getComputeService().templateBuilder().build();
      assertEquals(defaultTemplate.getImage().getOperatingSystem().is64Bit(), true);
      assertEquals(defaultTemplate.getImage().getOperatingSystem().getVersion(), "10.04");
      assertEquals(defaultTemplate.getImage().getOperatingSystem().getFamily(), OsFamily.UBUNTU);
      assertEquals(defaultTemplate.getLocation().getId(), provider);
      assertEquals(getCores(defaultTemplate.getHardware()), 1.0d);
   }

   @Override
   protected Set<String> getIso3166Codes() {
      return ImmutableSet.<String> of("US-IL", "US-TX");
   }
}
