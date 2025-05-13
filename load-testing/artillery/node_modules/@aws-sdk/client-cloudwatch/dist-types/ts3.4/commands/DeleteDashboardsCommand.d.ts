import { Command as $Command } from "@smithy/smithy-client";
import { MetadataBearer as __MetadataBearer } from "@smithy/types";
import {
  CloudWatchClientResolvedConfig,
  ServiceInputTypes,
  ServiceOutputTypes,
} from "../CloudWatchClient";
import {
  DeleteDashboardsInput,
  DeleteDashboardsOutput,
} from "../models/models_0";
export { __MetadataBearer };
export { $Command };
export interface DeleteDashboardsCommandInput extends DeleteDashboardsInput {}
export interface DeleteDashboardsCommandOutput
  extends DeleteDashboardsOutput,
    __MetadataBearer {}
declare const DeleteDashboardsCommand_base: {
  new (
    input: DeleteDashboardsCommandInput
  ): import("@smithy/smithy-client").CommandImpl<
    DeleteDashboardsCommandInput,
    DeleteDashboardsCommandOutput,
    CloudWatchClientResolvedConfig,
    ServiceInputTypes,
    ServiceOutputTypes
  >;
  new (
    input: DeleteDashboardsCommandInput
  ): import("@smithy/smithy-client").CommandImpl<
    DeleteDashboardsCommandInput,
    DeleteDashboardsCommandOutput,
    CloudWatchClientResolvedConfig,
    ServiceInputTypes,
    ServiceOutputTypes
  >;
  getEndpointParameterInstructions(): import("@smithy/middleware-endpoint").EndpointParameterInstructions;
};
export declare class DeleteDashboardsCommand extends DeleteDashboardsCommand_base {
  protected static __types: {
    api: {
      input: DeleteDashboardsInput;
      output: {};
    };
    sdk: {
      input: DeleteDashboardsCommandInput;
      output: DeleteDashboardsCommandOutput;
    };
  };
}
