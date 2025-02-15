/* SPDX-License-Identifier: MIT */

package li.cil.oc2r.common.bus.device.provider.item;

import li.cil.oc2r.api.bus.device.ItemDevice;
import li.cil.oc2r.api.bus.device.provider.ItemDeviceQuery;
import li.cil.oc2r.common.Config;
import li.cil.oc2r.common.bus.device.provider.util.AbstractItemDeviceProvider;
import li.cil.oc2r.common.bus.device.vm.item.NetworkTunnelDevice;
import li.cil.oc2r.common.item.Items;

import java.util.Optional;

public final class NetworkTunnelModuleItemDeviceProvider extends AbstractItemDeviceProvider {
    public NetworkTunnelModuleItemDeviceProvider() {
        super(Items.NETWORK_TUNNEL_MODULE);
    }

    ///////////////////////////////////////////////////////////////////

    @Override
    protected Optional<ItemDevice> getItemDevice(final ItemDeviceQuery query) {
        return Optional.of(new NetworkTunnelDevice(query.getItemStack()));
    }

    @Override
    protected int getItemDeviceEnergyConsumption(final ItemDeviceQuery query) {
        return Config.networkTunnelEnergyPerTick;
    }
}
